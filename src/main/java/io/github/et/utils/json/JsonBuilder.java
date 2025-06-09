package io.github.et.utils.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONReader;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.utils.lua.Item;
import io.github.et.utils.lua.LuaLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static io.github.et.Main.JSON_ALL;
import static io.github.et.Main.JSON_NO_GUIDE;

public class JsonBuilder {
    public static ArrayList<LuaLoader> luas = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean initialized = false;
    private static JSONObject existingConfig = null;

    public static void initAll() throws BotInfoNotFoundException, ClassNotFoundException {
        if (initialized) {
            return;
        }
        File configFile = new File("botInfo.json");
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                StringBuilder content = new StringBuilder();
                char[] buffer = new char[1024];
                int read;
                while ((read = reader.read(buffer)) != -1) {
                    content.append(buffer, 0, read);
                }
                JSONReader.Feature[] features = {
                    JSONReader.Feature.UseNativeObject,
                    JSONReader.Feature.FieldBased,
                    JSONReader.Feature.SupportArrayToBean
                };
                existingConfig = JSON.parseObject(content.toString(), JSONObject.class, features);
            } catch (IOException e) {
                System.err.println("读取配置文件失败：" + e.getMessage());
            }
        }
        
        ArrayList<String> luaFiles = findLuaFiles("./configs/addonConfigs");
        for (String luaFile : luaFiles) {
            luas.add(new LuaLoader(luaFile));
        }

        // 设置父功能引用
        for (LuaLoader lua : luas) {
            if (lua.getParent() != null) {
                luas.stream()
                    .filter(p -> p.getLuaName().equals(lua.getParent()))
                    .findFirst()
                    .ifPresent(lua::setParentLua);
            }
        }

        initialized = true;
    }

    public static ArrayList<String> findLuaFiles(String folderPath) {
        ArrayList<String> luaFiles = new ArrayList<>();
        File directory = new File(folderPath);
        if (!directory.exists() || !directory.isDirectory()) {
            return luaFiles;
        }
        traverseDirectory(directory, luaFiles);
        return luaFiles;
    }

    private static void traverseDirectory(File dir, ArrayList<String> result) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName().toLowerCase();
                if (fileName.endsWith(".lua")) {
                    result.add(file.getAbsolutePath());
                }
            } else if (file.isDirectory()) {
                traverseDirectory(file, result);
            }
        }
    }

    private static Object getValueFromUser(Item item) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty() && item.isNullable()) {
                    return null;
                }

                boolean typeSupported = false;
                for (Class<?> type : item.getClasses()) {
                    if (type == Boolean.class) {
                        if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                            return Boolean.parseBoolean(input);
                        }
                        typeSupported = true;
                    } else if (type == Integer.class) {
                        return Integer.parseInt(input);
                    } else if (type == Long.class) {
                        return Long.parseLong(input);
                    } else if (type == String.class) {
                        return input;
                    } else if (type == JSONArray.class) {
                        if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false")) {
                            return Boolean.parseBoolean(input);
                        } else if (!input.isEmpty()) {
                            return new JSONArray(java.util.Arrays.asList(input.split(",")));
                        }
                        typeSupported = true;
                    }
                }
                
                if (!typeSupported) {
                    System.out.println("不支持的类型，请检查类型定义");
                    continue;
                }
                
                System.out.println("输入类型不匹配，请重新输入");
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请重新输入");
            }
        }
    }

    private static boolean validateConfigValue(Object value, Item item) {
        if (value == null) {
            return item.isNullable();
        }

        for (Class<?> type : item.getClasses()) {
            if (type == Boolean.class) {
                if (value instanceof Boolean) {
                    return true;
                }
                if (value instanceof String) {
                    String strValue = (String) value;
                    return strValue.equalsIgnoreCase("true") || strValue.equalsIgnoreCase("false");
                }
            } else if (type == Integer.class) {
                if (value instanceof Integer) {
                    return true;
                }
                if (value instanceof String) {
                    try {
                        Integer.parseInt((String) value);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            } else if (type == Long.class) {
                if (value instanceof Long) {
                    return true;
                }
                if (value instanceof Integer) {
                    return true;
                }
                if (value instanceof String) {
                    try {
                        Long.parseLong((String) value);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                }
            } else if (type == String.class) {
                return value instanceof String;
            } else if (type == JSONArray.class) {
                return value instanceof JSONArray;
            }
        }
        return false;
    }

    private static void configureFeature(LuaLoader lua, JSONObject jsonObject, boolean includeRule) {
        String featureName = lua.getLuaName();
        JSONObject featureConfig = new JSONObject();
        
        if (includeRule) {
            if (!lua.getAllGuide().isEmpty()) {
                featureConfig.put("guide", lua.getAllGuide());
            }
            
            if (lua.getHelp() != null) {
                featureConfig.put("help", lua.getHelp());
            }
            if (lua.getLua().get("rule") != null) {
                featureConfig.put("rule", lua.getLua().get("rule").tojstring());
            }
        }

        featureConfig.put("include", new ArrayList<>());
        featureConfig.put("exclude", new ArrayList<>());

        for (Item item : lua.getAllItems()) {
            String itemName = item.getName();
            Object value = null;
            boolean needInput = true;
            
            if (existingConfig != null) {
                if (lua.getParent() != null) {
                    if (existingConfig.containsKey(lua.getParent()) && 
                        existingConfig.getJSONObject(lua.getParent()).containsKey(itemName)) {
                        value = existingConfig.getJSONObject(lua.getParent()).get(itemName);
                        if (validateConfigValue(value, item)) {
                            needInput = false;
                        }
                    }
                    if (existingConfig.containsKey(lua.getParent()) && 
                        existingConfig.getJSONObject(lua.getParent()).containsKey("children") &&
                        existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").containsKey(featureName) &&
                        existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").getJSONObject(featureName).containsKey(itemName)) {
                        value = existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").getJSONObject(featureName).get(itemName);
                        if (validateConfigValue(value, item)) {
                            needInput = false;
                        }
                    }
                } else {
                    if (existingConfig.containsKey(featureName) && 
                        existingConfig.getJSONObject(featureName).containsKey(itemName)) {
                        value = existingConfig.getJSONObject(featureName).get(itemName);
                        if (validateConfigValue(value, item)) {
                            needInput = false;
                        }
                    }
                }
            }
            
            if (needInput) {
                System.out.println("请输入 " + featureName + " 的 " + itemName + 
                    (item.isNullable() ? " (可为空)" : ""));
                value = getValueFromUser(item);
            }
            
            featureConfig.put(itemName, value);

            if (itemName.equals(featureName)) {
                if (Boolean.TRUE.equals(value)) {
                    if (existingConfig != null) {
                        if (lua.getParent() != null) {
                            if (existingConfig.containsKey(lua.getParent()) && 
                                existingConfig.getJSONObject(lua.getParent()).containsKey("exclude")) {
                                featureConfig.put("exclude", existingConfig.getJSONObject(lua.getParent()).getJSONArray("exclude"));
                            }
                            else if (existingConfig.containsKey(lua.getParent()) && 
                                existingConfig.getJSONObject(lua.getParent()).containsKey("children") &&
                                existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").containsKey(featureName) &&
                                existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").getJSONObject(featureName).containsKey("exclude")) {
                                featureConfig.put("exclude", existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").getJSONObject(featureName).getJSONArray("exclude"));
                            } else {
                                System.out.println("请输入要排除的群号（用英文逗号分隔）：");
                                String excludeInput = scanner.nextLine().trim();
                                if (!excludeInput.isEmpty()) {
                                    featureConfig.put("exclude", new ArrayList<>(java.util.Arrays.asList(excludeInput.split(","))));
                                }
                            }
                        } else {
                            if (existingConfig.containsKey(featureName) && 
                                existingConfig.getJSONObject(featureName).containsKey("exclude")) {
                                featureConfig.put("exclude", existingConfig.getJSONObject(featureName).getJSONArray("exclude"));
                            } else {
                                System.out.println("请输入要排除的群号（用英文逗号分隔）：");
                                String excludeInput = scanner.nextLine().trim();
                                if (!excludeInput.isEmpty()) {
                                    featureConfig.put("exclude", new ArrayList<>(java.util.Arrays.asList(excludeInput.split(","))));
                                }
                            }
                        }
                    }
                } else {
                    if (existingConfig != null) {
                        if (lua.getParent() != null) {
                            if (existingConfig.containsKey(lua.getParent()) && 
                                existingConfig.getJSONObject(lua.getParent()).containsKey("include")) {
                                featureConfig.put("include", existingConfig.getJSONObject(lua.getParent()).getJSONArray("include"));
                            }
                            else if (existingConfig.containsKey(lua.getParent()) && 
                                existingConfig.getJSONObject(lua.getParent()).containsKey("children") &&
                                existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").containsKey(featureName) &&
                                existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").getJSONObject(featureName).containsKey("include")) {
                                featureConfig.put("include", existingConfig.getJSONObject(lua.getParent()).getJSONObject("children").getJSONObject(featureName).getJSONArray("include"));
                            } else {
                                System.out.println("请输入要包含的群号（用英文逗号分隔）：");
                                String includeInput = scanner.nextLine().trim();
                                if (!includeInput.isEmpty()) {
                                    featureConfig.put("include", new ArrayList<>(java.util.Arrays.asList(includeInput.split(","))));
                                }
                            }
                        } else {
                            if (existingConfig.containsKey(featureName) && 
                                existingConfig.getJSONObject(featureName).containsKey("include")) {
                                featureConfig.put("include", existingConfig.getJSONObject(featureName).getJSONArray("include"));
                            } else {
                                System.out.println("请输入要包含的群号（用英文逗号分隔）：");
                                String includeInput = scanner.nextLine().trim();
                                if (!includeInput.isEmpty()) {
                                    featureConfig.put("include", new ArrayList<>(java.util.Arrays.asList(includeInput.split(","))));
                                }
                            }
                        }
                    }
                }
            }
        }

        if (lua.getParent() != null) {
            if (!jsonObject.containsKey(lua.getParent())) {
                jsonObject.put(lua.getParent(), new JSONObject());
            }
            if (!jsonObject.getJSONObject(lua.getParent()).containsKey("children")) {
                jsonObject.getJSONObject(lua.getParent()).put("children", new JSONObject());
            }
            jsonObject.getJSONObject(lua.getParent()).getJSONObject("children").put(featureName, featureConfig);
        } else {
            jsonObject.put(featureName, featureConfig);
        }
    }

    public static JSONObject buildJson() {
        if (!initialized) {
            try {
                initAll();
            } catch (BotInfoNotFoundException | ClassNotFoundException e) {
                System.err.println("初始化失败：" + e.getMessage());
                return new JSONObject();
            }
        }
        
        JSONObject jsonObject = new JSONObject();
        
        // 首先处理 Global 配置
        LuaLoader globalLua = luas.stream()
            .filter(lua -> lua.getLuaName().equals("Global"))
            .findFirst()
            .orElse(null);
            
        if (globalLua != null) {
            configureFeature(globalLua, jsonObject, false);
        }

        // 处理普通功能
        luas.stream()
            .filter(lua -> !lua.getLuaName().equals("Global") && !lua.isGame() && lua.getParent() == null)
            .forEach(lua -> configureFeature(lua, jsonObject, false));

        // 处理子功能
        luas.stream()
            .filter(lua -> !lua.getLuaName().equals("Global") && !lua.isGame() && lua.getParent() != null)
            .forEach(lua -> configureFeature(lua, jsonObject, false));

        // 处理游戏功能
        luas.stream()
            .filter(lua -> !lua.getLuaName().equals("Global") && lua.isGame())
            .forEach(lua -> configureFeature(lua, jsonObject, false));

        return jsonObject;
    }

    public static JSONObject buildFullJson() {
        if (!initialized) {
            try {
                initAll();
            } catch (BotInfoNotFoundException | ClassNotFoundException e) {
                System.err.println("初始化失败：" + e.getMessage());
                return new JSONObject();
            }
        }
        
        System.out.println("正在加载、构建配置...");
        JSONObject jsonObject = new JSONObject();
        
        // 首先处理 Global 配置
        LuaLoader globalLua = luas.stream()
            .filter(lua -> lua.getLuaName().equals("Global"))
            .findFirst()
            .orElse(null);
            
        if (globalLua != null) {
            configureFeature(globalLua, jsonObject, true);
        }

        // 处理普通功能
        luas.stream()
            .filter(lua -> !lua.getLuaName().equals("Global") && !lua.isGame() && lua.getParent() == null)
            .forEach(lua -> configureFeature(lua, jsonObject, true));

        // 处理子功能
        luas.stream()
            .filter(lua -> !lua.getLuaName().equals("Global") && !lua.isGame() && lua.getParent() != null)
            .forEach(lua -> configureFeature(lua, jsonObject, true));

        // 处理游戏功能
        luas.stream()
            .filter(lua -> !lua.getLuaName().equals("Global") && lua.isGame())
            .forEach(lua -> configureFeature(lua, jsonObject, true));

        return jsonObject;
    }

    public static void setValue(String key, Object obj){
        JSON_ALL.put(key, obj);
        JSON_NO_GUIDE.put(key, obj);
    }

    public static void update() throws IOException {
        if(JSON_ALL!=null&&JSON_NO_GUIDE!=null){
            File file = new File("./botInfo.json");
            if(!file.exists()){
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Object o1=JSON_NO_GUIDE.get("Global");
            if(o1 instanceof JSONObject||o1 instanceof com.alibaba.fastjson.JSONObject){
                Object o2=((JSONObject)o1).get("useGuide");
                if(o2 instanceof Boolean){
                    if(((Boolean) o2).booleanValue()){
                        writer.write(com.alibaba.fastjson.JSON.toJSONString(JSON_ALL, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
                    }else{
                        writer.write(com.alibaba.fastjson.JSON.toJSONString(JSON_NO_GUIDE, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue));
                    }
                    writer.flush();
                }
            }
            writer.close();
        }
    }
}
