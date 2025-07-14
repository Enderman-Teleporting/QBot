package io.github.et.subprocessLoader;

import io.github.et.exceptions.BotInfoNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ConfigLoader {
    public static ArrayList<MCServer> servers=new ArrayList<>();
    public static void load() throws IOException, BotInfoNotFoundException {
        File configFile=new File("./config.txt");
        if(configFile.exists()) {
            BufferedReader bf = new BufferedReader(new FileReader(configFile, StandardCharsets.UTF_8));
            for (String i:bf.lines().toList()){
                String[] a=i.split("\\|\\|");
                if(a.length==6){
                    servers.add(new MCServer(a[0],a[1],a[2],Long.parseLong(a[3]),Integer.parseInt(a[4]),a[5]));
                }else{
                    throw new BotInfoNotFoundException("Wrong arguments in config file");
                }

            }
        }
    }
}
