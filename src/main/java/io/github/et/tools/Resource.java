package io.github.et.tools;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.ettoolset.tools.logger.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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
                    Path path = Paths.get("").resolve(Paths.get(entry.getName()).getFileName().toString());
                    Files.createDirectories(path.getParent());
                    Files.copy(zis, path, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static void runInstaller(String exePath) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(exePath);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }));

        Thread outputConsumer = new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                while (process.getInputStream().read(buffer) != -1) {}
            } catch (IOException ignored) {}
        });
        outputConsumer.setDaemon(true);
        outputConsumer.start();

        if (!process.waitFor(30, TimeUnit.SECONDS)) {
            throw new RuntimeException("Process timed out");
        }

        if (process.isAlive()) {
            try (OutputStream os = process.getOutputStream()) {
                os.write('\n');
                os.flush();
                if (!process.waitFor(2, TimeUnit.SECONDS)) {
                    process.destroyForcibly();
                }
            }
        }
    }
    private static void deleteDirectory(File directory) {
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            if(files != null) {
                for(File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        if(!directory.delete()) {
            throw new RuntimeException("Failed to delete directory: " + directory.getAbsolutePath());
        }
    }

    public static void update() throws Exception {
        Logger logger=Logger.getDeclaredLogger();
        logger.info("正在检查更新，请稍等");
        File file = new File("NapCatInstaller.exe");
        if (!file.exists()) {
            extractNapcatZip();
        }
        File QQ=new File("QQ.exe");
        if (QQ.exists()) {
            QQ.delete();
        }
        for(File f: Objects.requireNonNull(file.getParentFile().listFiles())){
            if(f.isDirectory()&&f.getName().matches("NapCat\\..+\\.Shell")){
                deleteDirectory(f);
            }
        }
        runInstaller("./NapCatInstaller.exe");
        logger.info("正在配置...");
        for(File f: Objects.requireNonNull(file.getParentFile().listFiles())){
            if(f.isDirectory()&&f.getName().matches("NapCat\\..+\\.Shell")){
                File configDir = new File(f.getAbsolutePath()+"/versions");
                if(configDir.isDirectory()){
                    for(File version: Objects.requireNonNull(configDir.listFiles())){
                        if(version.isDirectory()&&version.getName().matches("[1-9]*\\.[1-9]*.[1-9]*-[1-9]*")){
                            final JSONObject global=(JSONObject) Main.JSON_NO_GUIDE.get("Global");
                            File config = new File(version.getAbsolutePath()+"/resources/app/napcat/config/onebot11_"+global.get("id")+".json");
                            if(!config.exists()){
                                config.createNewFile();
                                BufferedWriter bf=new BufferedWriter(new FileWriter(config));
                                final String json=
                                                "{\n" +
                                                "  \"network\": {\n" +
                                                "    \"httpServers\": [],\n" +
                                                "    \"httpClients\": [],\n" +
                                                "    \"websocketServers\": [\n" +
                                                "      {\n" +
                                                "        \"name\": \"WsServer\",\n" +
                                                "        \"enable\": true,\n" +
                                                "        \"host\": \"127.0.0.1\",\n" +
                                                "        \"port\": "+global.get("port")+",\n" +
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
        logger.info("检查更新完成！");
    }
    public static void checkFileValidity() throws BotInfoNotFoundException {
        File[] QQ = {new File("QQ.exe"),new File("napcat.bat"),new File("NapCatWinBootHook.dll"),new File("NapCatWinBootMain.exe")};
        for (File i :QQ) {
            if(!i.exists()){
                throw new BotInfoNotFoundException("缺少资源文件！请重启程序以重新下载资源");
            }
        }

    }

}
