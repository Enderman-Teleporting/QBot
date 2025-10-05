package io.github.et.tools;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.utils.json.JsonBuilder;
import io.github.et.utils.pluginLoader.lua.LuaConfig;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.LuajavaLib;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Resource {
    private static void extractNapcatZip() throws IOException {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("io/github/et/napcat_all.zip")) {
            if (is == null) return;
            try (ZipInputStream zis = new ZipInputStream(is)) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.isDirectory()) continue;
                    Path path = Paths.get("./" + entry.getName());
                    Files.createDirectories(path.getParent());
                    Files.copy(zis, path, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static void runInstaller() throws Exception {
        ProcessBuilder pb = new ProcessBuilder("cmd", "/C", "start", "\"./NapCatInstaller.exe\"", "\"./NapCatInstaller.exe\"");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }));
        System.out.println("NapCat安装程序已启动,请在安装完成后关闭安装程序并在此输入任意字符");
        System.out.println("如欲更新,请删除QQ.exe,NapCat.xxx.Shell文件夹");
        System.in.read();
    }

    private static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        if (!directory.delete()) {
            throw new RuntimeException("Failed to delete directory: " + directory.getAbsolutePath());
        }
    }

    public static void update() throws Exception {
        extractNapcatZip();
        Main.JSON_NO_GUIDE = JsonBuilder.buildJson();
        Main.JSON_ALL = JsonBuilder.buildFullJson();
        try {
            checkFileValidity();
        } catch (BotInfoNotFoundException e) {
            runInstaller();
        }
        System.out.println("正在配置...");
        File root = new File(".");

        for (File f : root.listFiles()) {
            if (f.isDirectory() && f.getName().matches("NapCat\\.[0-9]+\\.Shell")) {
                File launch = new File(f.getAbsolutePath() + "/napcat.quick.bat");
                if (!launch.exists()) {
                    launch.createNewFile();
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(launch))) {
                    bw.write("@echo off\n" +
                            "chcp 65001\n" +
                            ".\\NapCatWinBootMain.exe " + Main.JSON_NO_GUIDE.getJSONObject("Global").get("id") + "\n" +
                            "pause");
                    bw.flush();
                    bw.close();
                }
                File configDir = new File(f.getAbsolutePath() + "/versions");
                if (configDir.isDirectory()) {
                    for (File version : Objects.requireNonNull(configDir.listFiles())) {
                        if (version.isDirectory() && version.getName().matches("[0-9]+\\.[0-9]+\\.[0-9]+-[0-9]+")) {
                            final JSONObject global = Main.JSON_NO_GUIDE.getJSONObject("Global");
                            File config = new File(version.getAbsolutePath() + "/resources/app/napcat/config/onebot11_" + global.get("id") + ".json");
                            if (!config.exists()) {
                                config.createNewFile();
                            }
                            BufferedWriter bf = new BufferedWriter(new FileWriter(config));
                            final String json =
                                    "{\n" +
                                            "  \"network\": {\n" +
                                            "    \"httpServers\": [],\n" +
                                            "    \"httpClients\": [],\n" +
                                            "    \"websocketServers\": [\n" +
                                            "      {\n" +
                                            "        \"name\": \"WsServer\",\n" +
                                            "        \"enable\": true,\n" +
                                            "        \"host\": \"127.0.0.1\",\n" +
                                            "        \"port\": " + global.get("port") + ",\n" +
                                            "        \"messagePostFormat\": \"array\",\n" +
                                            "        \"reportSelfMessage\": false,\n" +
                                            "        \"token\": \"\",\n" +
                                            "        \"enableForcePushEvent\": true,\n" +
                                            "        \"debug\": false,\n" +
                                            "        \"heartInterval\": 300\n" +
                                            "      }\n" +
                                            "    ],\n" +
                                            "    \"websocketClients\": []\n" +
                                            "  },\n" +
                                            "  \"musicSignUrl\": \"\",\n" +
                                            "  \"enableLocalFile2Url\": true,\n" +
                                            "  \"parseMultMsg\": true\n" +
                                            "}";
                            bf.write(json);
                            bf.flush();
                            bf.close();
                            break;
                        }

                    }
                }
                createLuaFile4Plugins();
            }

        }

    }


    public static void checkFileValidity() throws BotInfoNotFoundException {
        boolean a = false;
        File file = new File("QQ.exe");
        if (file.exists()) {
            File root = new File(".");
            for (File i : root.listFiles()) {
                if (i.getName().matches("NapCat\\.[0-9]+\\.Shell") && i.isDirectory()) {
                    for (File j : i.listFiles()) {
                        if (j.getName().equalsIgnoreCase("napcat.quick.bat")) {
                            a = true;
                        }
                    }
                }
            }
        }
        if (!a) {
            throw new BotInfoNotFoundException("缺少资源文件!请删除QQ.exe,napcat.xxxx.shell文件夹以及相关zip文件后点击NapCatInstaller.exe重新手动安装!");
        }
    }

    private static void createLuaFile4Plugins() throws IOException, BotInfoNotFoundException {
        HashSet<String> names = new HashSet<>();
        for (LuaConfig i : LuaConfig.luaConfigArrayList) {
            String name = "./configs/addonConfigs/" + (i.getPackageName() + "." + i.getFeatureName()).replace(".", "/") + ".lua";
            File file = new File("./configs/addonConfigs/" + (i.getPackageName() + "." + i.getFeatureName()).replace(".", "/") + ".lua");
            if (!file.exists()) {
                file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                StringBuilder sb = new StringBuilder("return{\n");
                for (String j : i.getConfig().keySet()) {
                    Object a = i.getConfig().get(j);
                    if (a instanceof String) {
                        sb.append("\t").append(j).append("=\"").append(a).append("\",\n");
                    } else if (a instanceof LuaValue) {
                        if (a.equals(LuajavaLib.NIL)) {
                            sb.append("\t").append(j).append("=nil,\n");
                        }
                    } else if (a instanceof ArrayList n) {
                        sb.append("\t").append(j).append("={");
                        if (n.isEmpty()) {
                            sb.append("},\n");
                        }else{
                            if(n.get(0) instanceof String){
                                for (int m=0;m<n.size()-1;m++) {
                                    sb.append("\n\t\t\"").append(n.get(m)).append("\",\n");
                                }
                                sb.append("\n\t\t\"").append(n.get(n.size()-1)).append("\"\n\t},\n");
                            }else if(n.get(0) instanceof HashMap q){
                                for (int m=0;m<n.size()-1;m++) {
                                    sb.append("\n\t\t{");
                                    Object[] keySet = q.keySet().toArray();
                                    for (int k=0;k< keySet.length-1;k++) {
                                        sb.append("\"").append(keySet[k]).append("\"=\"").append(q.get(keySet[k])).append("\",");
                                    }
                                    sb.append("\"").append(keySet[keySet.length-1]).append("\"=\"").append(q.get(keySet[keySet.length-1])).append("\"},\n");
                                }
                                sb.append("\n\t\t{");
                                Object[] keySet = q.keySet().toArray();
                                for (int k=0;k< keySet.length-1;k++) {
                                    sb.append("\"").append(keySet[k]).append("\"=\"").append(q.get(keySet[k])).append("\",");
                                }
                                sb.append("\"").append(keySet[keySet.length-1]).append("\"=\"").append(q.get(keySet[keySet.length-1])).append("\"}\n\t}\n}");
                            }else{
                                throw new BotInfoNotFoundException("Unknown type");
                            }
                        }
                    }
                }
                bw.write(sb.toString());
                bw.flush();
                bw.close();
                names.add(name);
            }
            Files.walkFileTree(Paths.get("./configs/addonConfigs/"), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            if (!names.contains(file.toString())) {
                                Files.delete(file);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            if (!dir.equals(Paths.get("./configs/addonConfigs")) && Files.list(dir).count() == 0) {
                                Files.delete(dir);
                            }
                            return FileVisitResult.CONTINUE;
                        }

                    }
            );
        }
    }
}
