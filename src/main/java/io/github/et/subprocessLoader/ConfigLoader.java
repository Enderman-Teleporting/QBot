package io.github.et.subprocessLoader;

import java.io.*;
import java.util.ArrayList;

public class ConfigLoader {
    public static ArrayList<MCServer> servers=new ArrayList<>();
    public static void load() throws IOException {
        File configFile=new File("./config.txt");
        if(configFile.exists()) {
            BufferedReader bf = new BufferedReader(new FileReader(configFile));
            for (String i:bf.lines().toList()){
                String[] a=i.split("\\|\\|");
                servers.add(new MCServer(a[0],a[1],Long.parseLong(a[2])));
            }
        }
    }
}
