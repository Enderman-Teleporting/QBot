package io.github.et.eventListener;

import io.github.et.exceptions.messageExceptions.IllegalEventHandlingException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class GroupNameChangeEvent extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger.getDeclaredLogger().error("Error dealing with name changes, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalEventHandlingException("Exception occurred when dealing with group name change event",exception);
    }
    @EventHandler
    public void detectChange(net.mamoe.mirai.event.events.GroupNameChangeEvent event) throws LoggerNotDeclaredException {
        MessageChain message= new MessageChainBuilder()
                .append(new At(event.getOperator().getId()))
                .append("把群名从")
                .append(event.getOrigin())
                .append("改成了")
                .append(event.getNew())
                .build();
        event.getGroup().sendMessage(message);
        Logger logger=Logger.getDeclaredLogger();
        logger.info("Listened group name change event at: %s",event.getGroup().getId());
    }
}
