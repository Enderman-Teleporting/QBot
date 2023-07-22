package io.github.et.eventListener;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.tools.ServerSearching;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ChangeGroupName extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when handling a group name change request, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent",exception);
    }

    @EventHandler
    public void change(GroupMessageEvent msgEvent) throws LoggerNotDeclaredException {
        if(msgEvent.getMessage().contentToString().startsWith("群名称 ")){
            String message=msgEvent.getMessage().contentToString().replace("群名称 ","");
            msgEvent.getSubject().setName(message);
            Logger logger = Logger.getDeclaredLogger();
            logger.info("Handled a change-group-name request");
        }

    }
}
