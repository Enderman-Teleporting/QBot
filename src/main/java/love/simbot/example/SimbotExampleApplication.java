package love.simbot.example;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.annotation.SimbotResource;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.core.SimbotApp;
import love.forte.simbot.core.SimbotContext;
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
    public static void main(String[] args) throws IOException {
        Log_settler.settle();
        properties_settler.settle();
        SimbotContext a=SimbotApp.run(SimbotExampleApplication.class, args);
        Bot bot=a.getBotManager().getDefaultBot();
        for(;;) {
            Handler(bot);
        }
    }
}
