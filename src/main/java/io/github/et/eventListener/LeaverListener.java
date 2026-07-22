package io.github.et.eventListener;

import io.github.et.conopt4j.logger.Logger;
import io.github.et.utils.json.FeatureInUse;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public class LeaverListener extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Logger.error("Error dealing with exits");
    }
    @EventHandler
    public void onExit(MemberLeaveEvent event) {
        if (FeatureInUse.isInUse("Exit", event.getGroup().getId())) {
            event.getGroup().sendMessage("555～" + event.getMember().getNick() + "离开了我们...");
            Logger.error("Listened member leave event at: %s", event.getGroupId());
        }
    }
}
