package io.github.et;

import com.alibaba.fastjson2.JSONArray;
import io.github.et.eventListener.*;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.games.roulette.Roulette;
import io.github.et.messager.*;
import io.github.et.tools.CommandConsole;
import io.github.et.utils.JsonReader;
import io.github.ettoolset.tools.logger.LevelNotMatchException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import io.github.ettoolset.tools.logger.RepeatedLoggerDeclarationException;
import net.mamoe.mirai.Bot;
import org.fusesource.jansi.AnsiConsole;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import top.mrxiaom.overflow.BotBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

import static io.github.et.utils.JsonReader.jsonObject;

public class Main {
    public static Globals globals= JsePlatform.standardGlobals();
    public static String URL = "v1/chat/completions";
    public static String Image_URL = "v1/images/generations";
    public static String APIKEY;
    public static Bot bot;
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException, BotInfoNotFoundException, RepeatedLoggerDeclarationException, LevelNotMatchException {
        Logger logger;
        System.out.println("Trying to load bot info from ./botInfo.json");
        AnsiConsole.systemInstall();
        JsonReader.init();
        if (jsonObject.get("log") == null) {
            logger=new Logger(Logger.Levels.DEBUG,null);
        } else if(jsonObject.get("log").equals("null")){
            logger=new Logger(Logger.Levels.DEBUG,null);
        }else{
            logger=new Logger(Logger.Levels.DEBUG, (String) jsonObject.get("log"));
        }
        if(jsonObject.get("port")==null|| !(jsonObject.get("port") instanceof Number)) {
            System.out.println("请输入空闲端口");
            String ans = sc.nextLine();
            while (true) {
                if (ans == null) {
                    System.out.println("请重新输入");
                    ans=sc.nextLine();
                } else if (!ans.matches("^[0-9]+$")) {
                    System.out.println("请输入数字");
                    ans=sc.nextLine();
                } else {
                    if (Integer.parseInt(ans) > 65535 || Integer.parseInt(ans) < 0) {
                        System.out.println("请输入0-65535之间的合法端口");
                        ans=sc.nextLine();
                    } else {
                        jsonObject.put("port", Integer.parseInt(ans));
                    }
                }
            }
        }
//        if(jsonObject.get("id")==null||!(jsonObject.get("id") instanceof Number)){
//            System.out.println("请输入QQ号");
//            String ans=sc.nextLine();
//            while(true){
//                if(ans==null){
//                    System.out.println("请重新输入");
//                    ans=sc.nextLine();
//                }else if(!ans.matches("^[0-9]+$")){
//                    System.out.println("请输入数字");
//                    ans=sc.nextLine();
//                }else {
//                    jsonObject.put("id",Long.parseLong(ans));
//                }
//            }
//        }
//
//        sc.close();
//        //TODO add napcat download code
//        logger.debug("Initialized logger");
//        //Deamon.runDeamon(RunMethod.CONSOLE);
//        bot= BotBuilder.positive((String) jsonObject.get("host"))
//                .token((String) jsonObject.get("token"))
//                .connect();
//        if(bot==null){
//            throw new BotInfoNotFoundException();
//        }
//        bot.login();
//        if(jsonObject.get("doNudgeEvent") instanceof Boolean a){
//            if(a) {
//                bot.getEventChannel().registerListenerHost(new Nudger());
//                logger.fine("Registered listener Nudger");
//            }
//        }else if(jsonObject.get("doNudgeEvent") instanceof JSONArray a) {
//            bot.getEventChannel().registerListenerHost(new Nudger());
//            logger.fine("Registered listener Nudger");
//
//        }
//        if(jsonObject.get("repeat") instanceof Boolean a){
//            if(a) {
//                bot.getEventChannel().registerListenerHost(new Repeater());
//                logger.fine("Registered Listener Repeater");
//            }
//        }else if(jsonObject.get("repeat") instanceof JSONArray a) {
//            bot.getEventChannel().registerListenerHost(new Repeater());
//            logger.fine("Registered Listener Repeater");
//        }
//        if(jsonObject.get("reply")instanceof Boolean a){//TODO Null?JSONReader中直接加缺的搞成null或者[]
//            if (a) {
//                registerReplier();
//            }
//        }else if(jsonObject.get("reply") instanceof JSONArray a) {
//            registerReplier();
//        }
//        if(botInfo.get("PassAddRequest").equals("true")){
//            bot.getEventChannel().registerListenerHost(new RequestPasser());
//            logger.fine("Registered listener ApplicationPasser");
//        }
//        if(botInfo.get("GroupTalkative").equals("true")){
//            bot.getEventChannel().registerListenerHost(new GroupTalkativeChange());
//            logger.fine("Registered listener GroupTalkativeChange");
//        }
//        if(botInfo.get("MineServerStat").equals("true")){
//            bot.getEventChannel().registerListenerHost(new ServerSearcher());
//            logger.fine("Registered listener ServerSearcher");
//        }
//        if(botInfo.get("Admin").equals("true")){
//            bot.getEventChannel().registerListenerHost(new AdminBuffet());
//            logger.fine("Registered listener AdminBuffet");
//        }
//        if(botInfo.get("GroupNameChange").equals("true")){
//            bot.getEventChannel().registerListenerHost(new ChangeGroupName());
//            logger.fine("Registered listener ChangeGroupName");
//        }
//        if(botInfo.get("GroupName").equals("true")){
//            bot.getEventChannel().registerListenerHost(new GroupNameChangeEvent());
//            logger.fine("Registered listener GroupNameChangeEvent");
//        }
//        if(botInfo.get("Exit").equals("true")){
//            bot.getEventChannel().registerListenerHost(new LeaverListener());
//            logger.fine("Registered listener LeaverListener");
//        }
//        if(botInfo.get("Pistol").equals("true")){
//            bot.getEventChannel().registerListenerHost(new Roulette());
//            logger.fine("Registered listener Roulette");
//        }
//        if(botInfo.get("Image").equals("true")){
//            bot.getEventChannel().registerListenerHost(new ImageGenerator());
//            logger.fine("Registered listener ImageGenerator");
//        }
//        if(botInfo.get("FreeTalk").equals("true")){
//            bot.getEventChannel().registerListenerHost(new FreeTalk());
//        }
//
//        new Thread(() -> {
//            while(true){
//                try {
//                    CommandConsole.handle(bot);
//                } catch (LoggerNotDeclaredException | LevelNotMatchException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
//        bot.join();
//
//    }
//
//
//    private static void registerReplier(){
//        Object b;
//        if ((b = jsonObject.get("API_KEY")) != null) {
//            if (b instanceof String s) {
//                APIKEY = s;
//            } else {
//                System.out.println("请输入你的API-KEY");
//                APIKEY = sc.nextLine();
//            }
//        } else {
//            System.out.println("请输入你的API-KEY");
//            APIKEY = sc.nextLine();
//        }
//        Object url;
//        String urlPrefix;
//        if ((url = jsonObject.get("url")) != null) {
//            if (url instanceof String s) {
//                urlPrefix = s;
//            } else {
//                System.out.println("请输入你的请求地址");
//                urlPrefix = sc.nextLine();
//            }
//        }else{
//            System.out.println("请输入你的请求地址");
//            urlPrefix = sc.nextLine();
//        }
//        if(urlPrefix.endsWith("/")){
//            URL=urlPrefix+URL;
//            Image_URL = urlPrefix + Image_URL;
//        }else{
//            URL=urlPrefix+"/"+URL;
//            Image_URL = urlPrefix + "/" + Image_URL;
//        }
//        bot.getEventChannel().registerListenerHost(new Replier());
//        logger.fine("Registered listener Replier");
    }

}