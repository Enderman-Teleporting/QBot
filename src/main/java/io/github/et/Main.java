package io.github.et;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.tools.CommandConsole;
import io.github.et.tools.Resource;
import io.github.et.utils.classLoader.ClassLoader;
import io.github.et.utils.json.JsonBuilder;
import io.github.ettoolset.tools.logger.LevelNotMatchException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import net.mamoe.mirai.Bot;
import org.fusesource.jansi.AnsiConsole;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import top.mrxiaom.overflow.BotBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;


public class Main {
    public static Globals globals= JsePlatform.standardGlobals();
    public static String URL = "v1/chat/completions";
    public static String Image_URL = "v1/images/generations";
    public static String APIKEY;
    public static Bot bot;
    public static JSONObject JSON_ALL;
    public static JSONObject JSON_NO_GUIDE;
    private static Logger logger;
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
        bot= BotBuilder.positive("ws://127.0.0.1:"+((JSONObject)JSON_ALL.get("Global")).get("port")).connect();
        if(bot==null){
            throw new BotInfoNotFoundException();
        }
        bot.login();
        buildURL();
        logger.info("正在注册监听器……");
        List<Class<?>> clazz= ClassLoader.loadClasses();
        for (Class<?> c:clazz){
            c.getDeclaredConstructor().newInstance();
            logger.info("已注册监听器"+c.getName());
        }
        new Thread(() -> {
            while(true){
                try {
                    logger.fine(CommandConsole.handle(bot,CommandConsole.getCommand()));
                } catch (LoggerNotDeclaredException | LevelNotMatchException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        bot.join();
    }

    public static void buildURL(){
        String a = ((JSONObject) JSON_NO_GUIDE.get("Reply")).get("URL").toString();
        if(a.endsWith("/")){
            URL=a+URL;
            Image_URL=a+Image_URL;
        }else{
            URL=a+"/"+URL;
            Image_URL=a+"/"+Image_URL;
        }

    }



}