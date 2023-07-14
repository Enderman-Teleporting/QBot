package io.github.et;

import io.github.et.exceptions.BotInfoNotFoundException;
import io.github.ettoolset.tools.deamon.Deamon;
import io.github.ettoolset.tools.deamon.RunMethod;
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
    public static void main(String[] args) throws IOException, BotInfoNotFoundException, RepeatedLoggerDeclarationException {
        Logger logger=new Logger();
        Deamon.runDeamon(RunMethod.CONSOLE);
        Properties botInfo;
        logger.info("Trying to load bot info from ./botInfo.properties");
        try{
            botInfo=new Properties();
            botInfo.load(new BufferedReader(new FileReader("./botInfo.properties")));
            logger.info("Successfully loaded bot info");
        }catch(IOException e){
            new File("./botInfo.properties").createNewFile();
            logger.fatal();
            throw new BotInfoNotFoundException("Cannot load bot info from file: ./botInfo.properties");
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
        Runner.runAll(bot);
        bot.join();

    }

}