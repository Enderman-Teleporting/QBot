package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.utils.json.JsonBuilder;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;

public class getHelp extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when handling help generation operation, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent",exception);
    }
    @EventHandler
    public void generate(MessageEvent evt){
        if (evt.getMessage().contentToString().equals("帮助")) {
            evt.getSubject().sendMessage(JsonBuilder.generateHelp_list());
        } else if (evt.getMessage().contentToString().startsWith("帮助 ")) {
            evt.getSubject().sendMessage(JsonBuilder.generateHelp(evt.getMessage().contentToString().substring(3)));
        }
    }
}
