package io.github.et.eventListener;

import io.github.et.conopt4j.logger.Logger;
import io.github.et.exceptions.messageExceptions.IllegalEventHandlingException;
import io.github.et.utils.json.FeatureInUse;
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
        Logger.error("Exception occurred when handling an operation");
    }



    @EventHandler
    public void doAdmin(GroupMessageEvent msgEvent) throws InterruptedException {
        if (msgEvent.getMessage().contentToString().equals("我要管理")) {
            if (FeatureInUse.isInUse("Admin", msgEvent.getSubject().getId())) {
                if (msgEvent.getSender() instanceof NormalMember) {
                    NormalMember sender = (NormalMember) msgEvent.getSender();
                    sender.modifyAdmin(true);
                    msgEvent.getSubject().sendMessage("给了熬");
                    Thread.sleep(60000);
                    msgEvent.getSubject().sendMessage("忘说了，就给一分钟");
                    sender.modifyAdmin(false);
                }
                Logger.info("Received and handled Admin request from %s", msgEvent.getSubject().getId());
            } else {
                msgEvent.getSubject().sendMessage("功能未开启");
            }
        }


    }
}
