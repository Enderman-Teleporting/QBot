package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.utils.json.JsonBuilder;
import io.github.et.conopt4j.logger.Logger;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class GetHelp extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Logger.error("Exception occurred when handling help generation operation");
    }
    @EventHandler
    public void generate(MessageEvent evt){
        if (evt.getMessage().contentToString().equals("帮助")) {
            evt.getSubject().sendMessage(JsonBuilder.generateHelp_list()+"\n\n请输入“帮助 [功能名]”以获取该功能的帮助信息,注意空格");
        } else if (evt.getMessage().contentToString().startsWith("帮助 ")) {
            evt.getSubject().sendMessage(JsonBuilder.generateHelp(evt.getMessage().contentToString().substring(3)));
        }
    }
}
