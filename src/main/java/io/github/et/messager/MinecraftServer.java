package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.subprocessLoader.ConfigLoader;
import io.github.et.subprocessLoader.MCServer;
import io.github.et.subprocessLoader.ServerStream;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@SuppressWarnings("unused")
public class MinecraftServer extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when linking MC Server, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when linking MC Server",exception);
    }

    @EventHandler
    public void onMessage(GroupMessageEvent evt) throws IOException {
        long id= evt.getSubject().getId();
        for(MCServer ms: ConfigLoader.servers){
            if(ms.getGroup()==id){
                ServerStream.os.write(("["+ms.getName()+"]<"+evt.getSender().getNick()+">"+evt.getMessage().contentToString()+"\r\n").getBytes(StandardCharsets.UTF_8));
                ServerStream.os.flush();
            }
        }
    }

}
