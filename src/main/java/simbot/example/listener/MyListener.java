package simbot.example.listener;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.FriendMessageEvent;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class MyListener {
    public void open(){
        ProcessBuilder builder = new ProcessBuilder("java -Dfile.encoding=utf-8 -jar QBot.jar");

        try {

            Process process = builder.start();

        } catch (Exception e) {

            e.printStackTrace();

        }
        System.exit(0);
    }
}
