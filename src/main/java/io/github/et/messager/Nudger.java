package io.github.et.messager;

import io.github.et.conopt4j.logger.Logger;
import io.github.et.utils.json.FeatureInUse;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.action.Nudge;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@SuppressWarnings("unused")
public class Nudger extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Logger.error("Exception occurred when handling a nudging operation");
    }



    @EventHandler
    public void runNudge(NudgeEvent nudgeEvent) {
        if (FeatureInUse.isInUse("Nudge", nudgeEvent.getSubject().getId())) {
            String[] reply = {"哎呀,别戳啦,人家害羞", "说,你是不是个老变态？！", "你再戳,再戳一下试试", "贴贴", "6", "啊……啊……好热", "好痒……", "这么不要脸？！", "轻点……"};
            if (nudgeEvent.getTarget().getId() == nudgeEvent.getBot().getId()) {
                Nudge nudge = nudgeEvent.getFrom().nudge();
                nudge.sendTo(nudgeEvent.getSubject());
                nudgeEvent.getSubject().sendMessage(reply[new Random().nextInt(0, reply.length)]);
                Logger.info("Received and handled nudge event from %s", nudgeEvent.getSubject().getId());
            }
        }
    }
}
