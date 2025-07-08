package io.github.et;

import com.alibaba.fastjson2.JSONObject;
import io.github.et.eventListener.AdminBuffet;
import io.github.et.eventListener.ChangeGroupName;
import io.github.et.eventListener.LeaverListener;
import io.github.et.eventListener.RequestPasser;
import io.github.et.exceptions.BotInfoNotFoundException;
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
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;


public class Main {
    public static Globals globals= JsePlatform.standardGlobals();
    public static String URL = "v1/chat/completions";
    public static String Image_URL = "v1/images/generations";
    public static String APIKEY;
    public static Bot bot=null;
    public static JSONObject JSON_ALL;
    public static JSONObject JSON_NO_GUIDE;
    private static Logger logger;
    public static void main(String[] args) throws Exception {
        Logger logger;
        System.setProperty("console.encoding", "UTF-8");
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
        logger.info("输入任意字符完成登录");
        System.in.read();
        bot= BotBuilder.positive("ws://127.0.0.1:"+((JSONObject)JSON_ALL.get("Global")).get("port")).connect();
        if(bot==null){
            throw new BotInfoNotFoundException();
        }
        bot.login();
        buildURL();
        logger.info("正在注册监听器……");
        List<Class<?>> clazz= ClassLoader.loadClasses();
        clazz.add(AdminBuffet.class);
        clazz.add(ChangeGroupName.class);
        clazz.add(LeaverListener.class);
        clazz.add(RequestPasser.class);
        clazz.add(ChangeConfigListener.class);
        clazz.add(FreeTalk.class);
        clazz.add(FreeTalk.class);
        clazz.add(GetHelp.class);
        clazz.add(ImageGenerator.class);
        clazz.add(MinecraftServer.class);
        clazz.add(Nudger.class);
        clazz.add(Repeater.class);
        clazz.add(Replier.class);
        clazz.add(ServerSearcher.class);
        for (Class<?> c:clazz){
            Object abc=c.getDeclaredConstructor().newInstance();
            if (abc instanceof ListenerHost a){
                bot.getEventChannel().registerListenerHost(a);
            }
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
        APIKEY = JSON_ALL.getJSONObject("Reply").getString("APIKEY");

    }



}