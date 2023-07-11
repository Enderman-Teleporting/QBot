package io.github.et;

import io.github.et.messager.Nudger;
import io.github.et.messager.Repeater;
import net.mamoe.mirai.Bot;

public class Runner {
    public static void runAll(Bot bot){
        Nudger.runNudge(bot);
        Repeater.runRepeat(bot);
    }
}
