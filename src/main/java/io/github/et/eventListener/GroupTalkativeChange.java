package io.github.et.eventListener;


import io.github.et.exceptions.messageExceptions.IllegalEventHandlingException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupTalkativeChangeEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class GroupTalkativeChange extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger.getDeclaredLogger().error("Error dealing with GroupTalkativeChangeEvent, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalEventHandlingException("Exception occurred when dealing with a event",exception);
    }

    @EventHandler
    public void onGroupTalkativeChange(GroupTalkativeChangeEvent event) throws LoggerNotDeclaredException {
        Logger logger=Logger.getDeclaredLogger();
        MessageChain chain= new MessageChainBuilder()
                .append(new At(event.getPrevious().getId()))
                .append("的龙王被")
                .append(new At(event.getNow().getId()))
                .append("抢走啦！")
                .build();
        event.getGroup().sendMessage(chain);
        logger.info("Handled a GroupTalkativeChangeEvent at %s",event.getGroup().getId());
    }
}
