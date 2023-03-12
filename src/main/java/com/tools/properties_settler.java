package com.tools;
import java.io.*;
import java.util.Properties;


public class properties_settler {
    public static void settle() {
        new File("./cache/properties").mkdirs();
        String fileName = "./cache/properties/property.properties";
        try {
            File f = new File(fileName);
            if(!f.exists()){
                f.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter("./cache/properties/property.properties"));
                bw.write("Log=true\n");
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String path, String key) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        Properties properties = new Properties();
        properties.load(in);
        return (properties.getProperty(key));
    }
}