package io.github.et;

import io.github.et.eventListener.ApplicationPasser;
import io.github.et.eventListener.GroupTalkativeChange;
import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.et.messager.Nudger;
import io.github.et.messager.Repeater;
import io.github.et.messager.Replier;
import io.github.ettoolset.tools.deamon.Deamon;
import io.github.ettoolset.tools.deamon.RunMethod;
import io.github.ettoolset.tools.logger.LevelNotMatchException;
import io.github.ettoolset.tools.logger.Logger;
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
        Logger logger=new Logger(Logger.Levels.DEBUG,null);
        Deamon.runDeamon(RunMethod.CONSOLE);
        Properties botInfo;
        logger.info("Trying to load bot info from ./botInfo.properties");
        try{
            botInfo=new Properties();
            botInfo.load(new BufferedReader(new FileReader("./botInfo.properties")));
            logger.info("Successfully loaded bot info");
            logger.fine("");
        }catch(IOException e){
            new File("./botInfo.properties").createNewFile();
            logger.fatal("Bot loading error occurred. Error info as follows:");
            throw new BotInfoNotFoundException("Cannot load bot info from file: ./botInfo.properties");
        } catch (LevelNotMatchException e) {
            throw new RuntimeException(e);
        }
        Bot bot= BotFactory.INSTANCE.newBot(Long.parseLong(botInfo.getProperty("qq")), BotAuthorization.byQRCode(),botConfiguration -> {
            botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.MACOS);
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
        bot.getEventChannel().registerListenerHost(new Nudger());
        logger.fine("Registered listener Nudger");
        bot.getEventChannel().registerListenerHost(new Repeater());
        logger.fine("Registered Listener Repeater");
        bot.getEventChannel().registerListenerHost(new Replier());
        logger.fine("Registered listener Replier");
        bot.getEventChannel().registerListenerHost(new ApplicationPasser());
        logger.fine("Registered listener ApplicationPasser");
        bot.getEventChannel().registerListenerHost(new GroupTalkativeChange());
        logger.fine("Registered listener GroupTalkativeChange");
        bot.join();

    }

}