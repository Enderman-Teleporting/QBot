package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.tools.ServerSearching;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static io.github.et.tools.API.getApi;

@SuppressWarnings("unused")
public class ServerSearcher extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when handling a server searching operation, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent",exception);
    }

    @EventHandler
    public void search(MessageEvent msgEvent) throws LoggerNotDeclaredException {
        if(msgEvent.getMessage().contentToString().startsWith("查服 ")){
            String msg = msgEvent.getMessage().contentToString();
            msg=msg.replace("查服 ","");
            if(msg.contains(":")){
                String[] list=msg.split(":");
                msgEvent.getSubject().sendMessage(ServerSearching.search(list[0],Integer.parseInt(list[1])));
            }else {
                msgEvent.getSubject().sendMessage(ServerSearching.search(msg));
            }
            Logger logger=Logger.getDeclaredLogger();
            logger.info("Handled a server searching request");
        }

    }
}
