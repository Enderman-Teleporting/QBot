package io.github.et.eventListener;

import io.github.et.conopt4j.logger.Logger;
import io.github.et.exceptions.messageExceptions.IllegalEventHandlingException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class RequestPasser extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Logger.error("Error dealing with applications, error info as follows:");
        throw new IllegalEventHandlingException("Exception occurred when dealing with Application event",exception);
    }
    @EventHandler
    public void friendPasser(NewFriendRequestEvent event){
        event.accept();
        Logger.info("Accepted friend request: %s",event.getFromId());
    }

    @EventHandler
    public void groupPasser(MemberJoinRequestEvent event) {
        try {
            event.accept();
            Logger.info("Accepted member add request: %s", event.getFromId());
        }catch (Exception e){
            Logger.info("Tried to accept member add request: %s but failed",event.getFromId());
        }
    }

    @EventHandler
    public void groupInvitationPasser(BotInvitedJoinGroupRequestEvent event) {
        try {
            event.accept();
            Logger.info("Accepted group invitation: %s", event.getGroupId());
        }catch (Exception e){
            Logger.info("Tried to accept group invitation: %s but failed",event.getGroupId());
        }
    }
}
