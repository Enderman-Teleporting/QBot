package love.simbot.example;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
public class Log_settler {
    public static void settle() throws IOException {
        Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date1= formatter.format(date);
        File dir = new File("./cache/logs");
        dir.mkdirs();
        File log= new File ("./cache/logs/"+date1+".log");
        if (!log.exists()){
            log.createNewFile();
        }
    }
    public static void writelog(String content) throws IOException {
        Date date2=new Date();
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
        String date3= formatter2.format(date2);
        File log1= new File ("./cache/logs/"+date3+".log");
        BufferedWriter output= new BufferedWriter(new FileWriter(log1));
        output.write(content+"\n");
        output.close();
    }
}
