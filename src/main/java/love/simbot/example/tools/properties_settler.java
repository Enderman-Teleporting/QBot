package love.simbot.example.tools;
import net.mamoe.mirai.Bot;
import java.io.*;
import java.util.Properties;


public class properties_settler {
    public static void settle() {
        String fileName = ".\\cache\\property.properties";
        try {
            File f = new File(fileName);
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(".\\cache\\property.properties"));
            bw.write("Log=true\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        accountInfo();
    }

    public static String read(String path, String key) throws IOException {
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        Properties properties = new Properties();
        properties.load(in);
        return (properties.getProperty(key));
    }
    public static void accountInfo(){
        String file;
        for (int i=0;i< Bot.getInstances().size();i++){
            String bot= String.valueOf(Bot.getInstances().get(i).getId());
            file=".\\cache\\properties\\"+bot+".properties";
            try {
                File f = new File(file);
                f.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write("Pic=true\n300WordsBan=true\nallowGroupNameChanging=true\nallowChangeTitle=true\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}