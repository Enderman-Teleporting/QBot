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


    public static void update() throws Exception {
        extractNapcatZip();
        Main.JSON_NO_GUIDE = JsonBuilder.buildJson();
        Main.JSON_ALL = JsonBuilder.buildFullJson();
        System.out.println("正在配置...");
        File root = new File(".");
        String osName = System.getProperty("os.name");
        if (osName != null && osName.startsWith("Windows")) {
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
                }

            }

        }


    }

    public static boolean checkFileValidity() throws BotInfoNotFoundException {
        File root = new File(".");
        for (File i : root.listFiles()) {
            if (i.getName().matches("NapCat\\.[0-9]+\\.Shell") && i.isDirectory()) {
                for (File j : i.listFiles()) {
                    if (j.getName().equalsIgnoreCase("napcat.quick.bat")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

