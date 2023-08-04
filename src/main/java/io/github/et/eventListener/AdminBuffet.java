package io.github.et.eventListener;

import io.github.et.exceptions.messageExceptions.IllegalEventHandlingException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public class AdminBuffet extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try{
            Logger logger= Logger.getDeclaredLogger();
            logger.error("Exception occurred when handling an operation, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalEventHandlingException("Exceptions found while handling AdminBuffet event",exception);
    }



    @EventHandler
    public void doAdmin(GroupMessageEvent msgEvent) throws LoggerNotDeclaredException, InterruptedException {
        if(msgEvent.getMessage().contentToString().equals("我要管理")){
            if(msgEvent.getSender() instanceof NormalMember){
                NormalMember sender=(NormalMember)msgEvent.getSender();
                sender.modifyAdmin(true);
                msgEvent.getSubject().sendMessage("给了熬");
                Thread.sleep(60000);
                msgEvent.getSubject().sendMessage("忘说了，就给一分钟");
                sender.modifyAdmin(false);
            }
            Logger logger=Logger.getDeclaredLogger();
            logger.info("Received and handled Admin request from %s",msgEvent.getSubject().getId());
        }

    }
}
