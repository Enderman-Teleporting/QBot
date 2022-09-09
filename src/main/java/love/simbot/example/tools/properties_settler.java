package love.simbot.example.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class properties_settler {
    public static void settle(){
        String fileName=".\\cache\\property.properties" ;
        try{
            File f = new File(fileName);
            f.createNewFile();
            BufferedWriter bw=new BufferedWriter(new FileWriter(".\\cache\\property.properties"));
            bw.write("Log=true\nName=菲菲");
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
