package love.simbot.example;

import org.jetbrains.annotations.NotNull;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.annotation.SimbotResource;
import love.forte.simbot.api.sender.BotSender;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;

@SimbotApplication({
        @SimbotResource(value = "simbot.yml", orIgnore = true),
        @SimbotResource(value = "simbot-dev.yml", orIgnore = true, command = "dev"),
})
// @SimbotApplication
public class SimbotExampleApplication {
    public static void main(String[] args) {
        /*
            run方法的第一个参数是一个标注了@SimbotApplication注解的启动类。
            第二个参数是main方法得到的启动参数。
         */
        SimbotApp.run(SimbotExampleApplication.class, args);
    }
}
