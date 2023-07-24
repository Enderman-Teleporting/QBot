package io.github.et;

import io.github.et.eventListener.*;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.games.roulette.Roulette;
import io.github.et.games.roulette.RussianPistol;
import io.github.et.messager.*;
import io.github.et.tools.CommandConsole;
import io.github.ettoolset.tools.deamon.Deamon;
import io.github.ettoolset.tools.deamon.RunMethod;
import io.github.ettoolset.tools.logger.LevelNotMatchException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import io.github.ettoolset.tools.logger.RepeatedLoggerDeclarationException;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
public class Main {
    public static void main(String[] args) throws IOException, BotInfoNotFoundException, RepeatedLoggerDeclarationException, LevelNotMatchException {
        System.out.println("Trying to load bot info from ./botInfo.properties");
        Properties botInfo;
        try{
            botInfo=new Properties();
            botInfo.load(new BufferedReader(new FileReader("./botInfo.properties")));
            System.out.println("Successfully loaded bot info");
        }catch(IOException e) {
            new File("./botInfo.properties").createNewFile();
            throw new BotInfoNotFoundException("Cannot load bot info from file: ./botInfo.properties");
        }
        Logger logger=new Logger(Logger.Levels.DEBUG,botInfo.getProperty("Log"));
        logger.debug("Initialized logger");
        Deamon.runDeamon(RunMethod.CONSOLE);
        Bot bot= BotFactory.INSTANCE.newBot(Long.parseLong(botInfo.getProperty("qq")), BotAuthorization.byQRCode(),botConfiguration -> {
            botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_WATCH);
            botConfiguration.setHeartbeatStrategy(BotConfiguration.HeartbeatStrategy.REGISTER);
            botConfiguration.setWorkingDir(new File("."));
            botConfiguration.setCacheDir(new File("cache"));
            File file=new File("./deviceInfo");
            if (!file.exists()){
                file.mkdir();
            }
            botConfiguration.fileBasedDeviceInfo("deviceInfo/device.json");

        });
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
            logger.fine("Registered listener Replier");
        }
        if(botInfo.get("PassAddRequest").equals("true")){
            bot.getEventChannel().registerListenerHost(new ApplicationPasser());
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

        Thread thread=new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try {
                        CommandConsole.handle(bot);
                    } catch (LoggerNotDeclaredException | LevelNotMatchException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        bot.join();

    }

}