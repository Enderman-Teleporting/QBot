package io.github.et;

import io.github.et.eventListener.*;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.games.roulette.Roulette;
import io.github.et.messager.*;
import io.github.et.tools.CommandConsole;
import io.github.ettoolset.tools.logger.LevelNotMatchException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import io.github.ettoolset.tools.logger.RepeatedLoggerDeclarationException;
import net.mamoe.mirai.Bot;
import org.fusesource.jansi.AnsiConsole;
import top.mrxiaom.overflow.BotBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static String URL = "v1/chat/completions";
    public static String Image_URL = "v1/images/generations";
    public static String APIKEY;
    public static Properties botInfo;
    public static Bot bot;
    public static void main(String[] args) throws IOException, BotInfoNotFoundException, RepeatedLoggerDeclarationException, LevelNotMatchException {
        Logger logger;
        System.out.println("Trying to load bot info from ./botInfo.properties");
        AnsiConsole.systemInstall();
        try{
            botInfo=new Properties();
            botInfo.load(new BufferedReader(new FileReader("./botInfo.properties")));
            System.out.println("Successfully loaded bot info");
        }catch(IOException e) {
            new File("./botInfo.properties").createNewFile();
            throw new BotInfoNotFoundException("Cannot load bot info from file: ./botInfo.properties");
        }
        if (botInfo.getProperty("Log") == null) {
            logger=new Logger(Logger.Levels.DEBUG,null);
        }
        else if(botInfo.getProperty("Log").equals("null")){
            logger=new Logger(Logger.Levels.DEBUG,null);
        }else{
            logger=new Logger(Logger.Levels.DEBUG,botInfo.getProperty("Log"));
        }

        logger.debug("Initialized logger");
        //Deamon.runDeamon(RunMethod.CONSOLE);
        bot= BotBuilder.positive(botInfo.getProperty("host"))
                .token(botInfo.getProperty("token"))
                .connect();
        if(bot==null){
            throw new BotInfoNotFoundException();
        }
        bot.login();
        if(botInfo.get("DoReplyNudgeEvent").equals("true")){
            bot.getEventChannel().registerListenerHost(new Nudger());
            logger.fine("Registered listener Nudger");
        }
        if(botInfo.get("Repeat").equals("true")){
            bot.getEventChannel().registerListenerHost(new Repeater());
            logger.fine("Registered Listener Repeater");
        }
        if(botInfo.get("Reply").equals("true")){
            bot.getEventChannel().registerListenerHost(new Replier());
            APIKEY= botInfo.getProperty("API_KEY","");
            String urlPrefix= botInfo.getProperty("Url","");
            if(urlPrefix.endsWith("/")){
                URL=urlPrefix+URL;
                Image_URL = urlPrefix + Image_URL;
            }else{
                URL=urlPrefix+"/"+URL;
                Image_URL = urlPrefix + "/" + Image_URL;
            }
            logger.fine("Registered listener Replier");
        }
        if(botInfo.get("PassAddRequest").equals("true")){
            bot.getEventChannel().registerListenerHost(new RequestPasser());
            logger.fine("Registered listener ApplicationPasser");
        }
        if(botInfo.get("GroupTalkative").equals("true")){
            bot.getEventChannel().registerListenerHost(new GroupTalkativeChange());
            logger.fine("Registered listener GroupTalkativeChange");
        }
        if(botInfo.get("MineServerStat").equals("true")){
            bot.getEventChannel().registerListenerHost(new ServerSearcher());
            logger.fine("Registered listener ServerSearcher");
        }
        if(botInfo.get("Admin").equals("true")){
            bot.getEventChannel().registerListenerHost(new AdminBuffet());
            logger.fine("Registered listener AdminBuffet");
        }
        if(botInfo.get("GroupNameChange").equals("true")){
            bot.getEventChannel().registerListenerHost(new ChangeGroupName());
            logger.fine("Registered listener ChangeGroupName");
        }
        if(botInfo.get("GroupName").equals("true")){
            bot.getEventChannel().registerListenerHost(new GroupNameChangeEvent());
            logger.fine("Registered listener GroupNameChangeEvent");
        }
        if(botInfo.get("Exit").equals("true")){
            bot.getEventChannel().registerListenerHost(new LeaverListener());
            logger.fine("Registered listener LeaverListener");
        }
        if(botInfo.get("Pistol").equals("true")){
            bot.getEventChannel().registerListenerHost(new Roulette());
            logger.fine("Registered listener Roulette");
        }
        if(botInfo.get("Image").equals("true")){
            bot.getEventChannel().registerListenerHost(new ImageGenerator());
            logger.fine("Registered listener ImageGenerator");
        }
        if(botInfo.get("FreeTalk").equals("true")){
            bot.getEventChannel().registerListenerHost(new FreeTalk());
        }

        new Thread(() -> {
            while(true){
                try {
                    CommandConsole.handle(bot);
                } catch (LoggerNotDeclaredException | LevelNotMatchException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        bot.join();

    }

}