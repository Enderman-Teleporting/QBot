package io.github.et.tools;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.utils.json.JsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Scanner;
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
        ProcessBuilder pb = new ProcessBuilder("cmd","/C","start","\"./NapCatInstaller.exe\"","\"./NapCatInstaller.exe\"");
        pb.redirectErrorStream(true);
        Process process = pb.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }));
        System.out.println("NapCat安装程序已启动,请在安装完成后关闭安装程序并在此输入任意字符");
        System.out.println("如欲更新,请删除QQ.exe,NapCat.xxx.Shell文件夹");
        Scanner sc=new Scanner(System.in);
        sc.next();
    }
    private static void deleteDirectory(File directory) {
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            if(files != null) {
                for(File file : files) {
                    if(file.isDirectory()) {
                        deleteDirectory(file);
                    }else{
                        file.delete();
                    }
                }
            }
        }
        if(!directory.delete()) {
            throw new RuntimeException("Failed to delete directory: " + directory.getAbsolutePath());
        }
    }

    public static void update() throws Exception {
        File file = new File("./NapCatInstaller.exe");
        if (!file.exists()) {
            extractNapcatZip();
        }
        Main.JSON_NO_GUIDE = JsonBuilder.buildJson();
        Main.JSON_ALL = JsonBuilder.buildFullJson();
        try {
            checkFileValidity();
        }catch (BotInfoNotFoundException e){
            runInstaller();
        }
        System.out.println("正在配置...");
        File root = new File(("./"));

        for (File f : root.listFiles()) {
            if (f.isDirectory() && f.getName().matches("NapCat\\.[0-9]+\\.Shell")) {
                File configDir = new File(f.getAbsolutePath() + "/versions");
                if (configDir.isDirectory()) {
                    for (File version : Objects.requireNonNull(configDir.listFiles())) {
                        if (version.isDirectory() && version.getName().matches("[0-9]+\\.[0-9]+\\.[0-9]+-[0-9]+")) {
                            final JSONObject global = Main.JSON_NO_GUIDE.getJSONObject("Global");
                            File config = new File(version.getAbsolutePath() + "/resources/app/napcat/config/onebot11_" + global.get("id") + ".json");
                            if (!config.exists()) {
                                config.createNewFile();
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
                                                "        \"heartInterval\": 3000\n" +
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
                            break;
                        }
                    }
                }
                break;
            }
        }
    }



    public static void checkFileValidity() throws BotInfoNotFoundException {
        boolean a=false;
        File file=new File("QQ.exe");
        if(file.exists()){
            File root = new File(".");
           for(File i:root.listFiles()){
               if(i.getName().matches("NapCat\\.[0-9]+\\.Shell")&&i.isDirectory()){
                   for(File j:i.listFiles()){
                       if(j.getName().equalsIgnoreCase("napcat.bat")){
                           a=true;
                       }
                   }
               }
           }
        }
        if(!a){throw new BotInfoNotFoundException("缺少资源文件!请删除QQ.exe,napcat.xxxx.shell文件夹以及相关zip文件后点击NapCatInstaller.exe重新手动安装!");}
    }

}
