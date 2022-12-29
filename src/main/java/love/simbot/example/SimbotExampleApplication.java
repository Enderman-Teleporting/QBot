package love.simbot.example;

import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.annotation.SimbotResource;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.core.SimbotApp;
import love.simbot.example.tools.Log_settler;
import love.simbot.example.tools.properties_settler;

import java.io.IOException;

import static love.simbot.example.tools.cmd.Handler;


@SimbotApplication({
        @SimbotResource(value = "simbot.yml", orIgnore = true),
        @SimbotResource(value = "simbot-dev.yml", orIgnore = true, command = "dev"),
})
// @SimbotApplication
public class SimbotExampleApplication {
    @Depend private static BotManager manager;
    public static void main(String[] args) throws IOException {
        Log_settler.settle();
        properties_settler.settle();
        SimbotApp.run(SimbotExampleApplication.class, args);
        for(;;) {
            Handler(manager.getDefaultBot());
        }
    }
}
