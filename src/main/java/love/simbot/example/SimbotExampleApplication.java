package love.simbot.example;

import love.forte.simbot.annotation.SimbotApplication;
import love.forte.simbot.annotation.SimbotResource;
import love.forte.simbot.core.SimbotApp;
import love.simbot.example.Log_settler;

import java.io.IOException;

@SimbotApplication({
        @SimbotResource(value = "simbot.yml", orIgnore = true),
        @SimbotResource(value = "simbot-dev.yml", orIgnore = true, command = "dev"),
})
// @SimbotApplication
public class SimbotExampleApplication {
    public static void main(String[] args) throws IOException {
        Log_settler.settle();
        SimbotApp.run(SimbotExampleApplication.class, args);
    }
}
