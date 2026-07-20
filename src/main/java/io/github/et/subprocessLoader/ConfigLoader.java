package io.github.et.subprocessLoader;

import io.github.et.exceptions.BotInfoNotFoundException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ConfigLoader {
    public static ArrayList<MCServer> servers=new ArrayList<>();
    public static void load() throws IOException, BotInfoNotFoundException {
        File file=new File("./mcservers");
        if(!file.exists()){
            return;
        }
        for (File i:file.listFiles()){
            if(i.getName().toLowerCase().endsWith(".properties")){
                Properties a=new Properties();
                a.load(new FileReader(i));
                servers.add(new MCServer(a.getProperty("name"), a.getProperty("working_dir"), a.getProperty("command"), Long.parseLong(a.getProperty("group")), Boolean.parseBoolean(a.getProperty("useBackup")),Boolean.parseBoolean(a.getProperty("death_msg")), Boolean.parseBoolean(a.getProperty("useAdvancement"))));
            }
        }
    }
}
