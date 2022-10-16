package love.simbot.example.tools;
import java.io.*;
import java.util.Properties;


public class properties_settler {
    public static void settle() {
        String fileName = "./cache/property.properties";
        try {
            File f = new File(fileName);
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter("./cache/property.properties"));
            bw.write("Log=true\n");
            bw.close();
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