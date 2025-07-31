package io.github.et;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.eventListener.AdminBuffet;
import io.github.et.eventListener.ChangeGroupName;
import io.github.et.eventListener.LeaverListener;
import io.github.et.eventListener.RequestPasser;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.games.roulette.Roulette;
import io.github.et.games.wordle.Wordle;
import io.github.et.messager.*;
import io.github.et.subprocessLoader.Loader;
import io.github.et.subprocessLoader.ServerStream;
import io.github.et.tools.CommandConsole;
import io.github.et.tools.Resource;
import io.github.et.utils.classLoader.ClassLoader;
import io.github.et.utils.json.JsonBuilder;
import io.github.ettoolset.tools.logger.LevelNotMatchException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.ListenerHost;
import org.fusesource.jansi.AnsiConsole;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import top.mrxiaom.overflow.BotBuilder;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;


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
        Logger logger;
        AnsiConsole.systemInstall();
        Resource.update();
        Resource.checkFileValidity();
        JsonBuilder.update();
        File file=new File("plugins");
        File file1 = new File("configs/addonConfigs");
        if(!file.exists()){
            file.mkdirs();
        }
        if(!file1.exists()){
            file1.mkdirs();
        }
        if (JSON_NO_GUIDE.get("log") == null) {
            logger=new Logger(Logger.Levels.DEBUG,null);
        } else if(JSON_NO_GUIDE.get("log").equals("null")){
            logger=new Logger(Logger.Levels.DEBUG,null);
        }else{
            logger=new Logger(Logger.Levels.DEBUG, (String)JSON_NO_GUIDE.get("log"));
        }
        new Thread(new Loader()).start();


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