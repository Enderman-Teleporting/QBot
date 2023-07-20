package io.github.et.eventListener;

import io.github.et.exceptions.messageExceptions.IllegalEventHandlingException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ApplicationPasser extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger.getDeclaredLogger().error("Error dealing with applications, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalEventHandlingException("Exception occurred when dealing with Application event",exception);
    }
    @EventHandler
    public void friendPasser(NewFriendRequestEvent event) throws LoggerNotDeclaredException {
        Logger logger=Logger.getDeclaredLogger();
        event.accept();
        logger.info("Accepted friend request: %s",event.getFromId());
    }

    @EventHandler
    public void groupPasser(MemberJoinRequestEvent event) throws LoggerNotDeclaredException {
        Logger logger=Logger.getDeclaredLogger();
        try {
            event.accept();
            logger.info("Accepted member add request: %s", event.getFromId());
        }catch (Exception e){
            logger.info("Tried to accept member add request: %s but failed",event.getFromId());
        }
    }
}
