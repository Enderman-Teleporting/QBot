package io.github.et;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.conopt4j.launcher.Launcher;
import io.github.et.subprocessLoader.Loader;
import io.github.et.tools.Resource;
import io.github.et.utils.json.JsonBuilder;
import net.mamoe.mirai.Bot;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.*;


public class Main {
    public static Globals globals= JsePlatform.standardGlobals();
    public static String URL = "/chat/completions";
    public static String Image_URL = "/images/generations";
    public static String APIKEY;
    public static Bot bot=null;
    public static JSONObject JSON_ALL;
    public static JSONObject JSON_NO_GUIDE;
    public static Console console=System.console();;

    public static void main(String[] args) throws Exception {
        File file=new File("log.properties");
        if(!file.exists()){
            file.createNewFile();
            BufferedWriter bw=new BufferedWriter(new FileWriter(file));
            bw.write("conopt4j.logger.format = Style.HINT\n" +
                    "conopt4j.logger.level = Level.DEBUG\n" +
                    "conopt4j.logger.info = Color.WHITE\n" +
                    "conopt4j.logger.warn = Color.YELLOW\n" +
                    "conopt4j.logger.debug = Color.CYAN\n" +
                    "conopt4j.logger.error = Color.RED\n" +
                    "conopt4j.logger.fatal = Color.PURPLE\n" +
                    "conopt4j.logger.severe = Color.RED\n" +
                    "conopt4j.logger.fine = Color.BLUE\n" +
                    "conopt4j.logger.useTime = false\n" +
                    "conopt4j.logger.useTrace = true\n" +
                    "conopt4j.logger.maxHistory = 1024\n" +
                    "conopt4j.command.prompt = >");
        }
        Launcher.init(new FileInputStream(file));
        Resource.update();
        JsonBuilder.update();
        File file0 =new File("plugins");
        File file1 = new File("configs/addonConfigs");
        if(!file0.exists()){
            file0.mkdirs();
        }
        if(!file1.exists()){
            file1.mkdirs();
        }
        new Thread(new Loader()).start();
        new Thread(new HeartBeat()).start();

    }

    public static void buildURL(){
        String a = ((JSONObject) JSON_NO_GUIDE.get("Reply")).get("URL").toString();
        if(a.endsWith("/")){
            URL=a+"chat/completions";
            Image_URL=a+"images/generations";
        }else{
            URL=a+"/chat/completions";
            Image_URL=a+"/images/generations";
        }
        APIKEY = JSON_ALL.getJSONObject("Reply").getString("APIKEY");

    }



}