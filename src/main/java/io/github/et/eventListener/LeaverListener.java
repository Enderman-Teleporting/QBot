package io.github.et.eventListener;

import io.github.et.exceptions.messageExceptions.IllegalEventHandlingException;
import io.github.et.utils.json.FeatureInUse;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public class LeaverListener extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger.getDeclaredLogger().error("Error dealing with exits, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalEventHandlingException("Exception occurred when a group leaver is found",exception);
    }
    @EventHandler
    public void onExit(MemberLeaveEvent event) throws LoggerNotDeclaredException {
        if (FeatureInUse.isInUse("Exit", event.getGroup().getId())) {
            event.getGroup().sendMessage("555～" + event.getMember().getNick() + "离开了我们...");
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Listened member leave event at: %s", event.getGroupId());
        }
    }
}
