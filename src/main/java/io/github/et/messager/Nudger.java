package io.github.et.messager;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.action.Nudge;

import java.util.Random;

public class Nudger {
    public static void runNudge(Bot bot){
        String reply[]={"哎呀,别戳啦,人家害羞","说,你是不是个老变态?!","你再戳,在戳一下试试","贴贴","6"};
        bot.getEventChannel().subscribeAlways(NudgeEvent.class,nudgeEvent -> {
           Nudge nudge=nudgeEvent.getFrom().nudge();
           nudge.sendTo(nudgeEvent.getSubject());
           nudgeEvent.getSubject().sendMessage(reply[new Random().nextInt(0, reply.length-1)]);
        });
    }
}
