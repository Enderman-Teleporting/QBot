package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.tools.GPT;
import io.github.et.utils.json.FeatureInUse;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;


@SuppressWarnings("unused")
public class Replier extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when handling a reply operation, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent",exception);
    }

    @EventHandler
    public void groupTalk(GroupMessageEvent msgEvent) throws LoggerNotDeclaredException {
        if(FeatureInUse.isInUse("Reply",msgEvent.getSubject().getId())) {
            if (msgEvent.getMessage().contains(new At(msgEvent.getBot().getId()))) {
                MessageChain msg = msgEvent.getMessage();
                String result = GPT.getReply(msgEvent.getSubject().getId(), msgProcess(msg));
                MessageChain chain = new MessageChainBuilder()
                        .append(result)
                        .append(new At(msgEvent.getSender().getId()))
                        .build();
                msgEvent.getGroup().sendMessage(chain);
                Logger logger = Logger.getDeclaredLogger();
                logger.info("Handled chatting event from Group: %s", msgEvent.getGroup().getId());
            }
        }

    }
    @EventHandler
    public void privateTalk(FriendMessageEvent msgEvent) throws IOException, LoggerNotDeclaredException {
        if(FeatureInUse.isInUse("Reply",msgEvent.getSubject().getId())) {
            if (!(msgEvent.getMessage().contentToString().startsWith("/")||msgEvent.getMessage().contentToString().startsWith("绘图 ") || msgEvent.getMessage().contentToString().startsWith("查服 "))) {
                MessageChain msg = msgEvent.getMessage();
                String a = msgProcess(msg);
                String result = a.equals("你好,请发送纯文本消息,谢谢") ? a : GPT.getReply(msgEvent.getSubject().getId(), a);
                msgEvent.getSubject().sendMessage(result);
                Logger logger = Logger.getDeclaredLogger();
                logger.info("Handled message reply at" + msgEvent.getSubject().getId());
            }
        }
    }

    public static String msgProcess(MessageChain msg){
        StringBuilder textMessage = new StringBuilder();
        boolean noText=true;
        for (SingleMessage messageContent : msg) {
            if (messageContent instanceof PlainText) {
                PlainText plainText = (PlainText) messageContent;
                textMessage.append(plainText.getContent());
                noText=false;
            }
        }
        if(noText) {
            textMessage.append("你好,请发送纯文本消息,谢谢");
        }
        return textMessage.toString();
    }




}
