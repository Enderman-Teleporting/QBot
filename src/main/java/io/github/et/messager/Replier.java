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
        //throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent",exception);
    }

    @EventHandler
    public void groupTalk(GroupMessageEvent msgEvent) throws LoggerNotDeclaredException {
        if (msgEvent.getMessage().contains(new At(msgEvent.getBot().getId()))) {
            if (FeatureInUse.isInUse("Reply", msgEvent.getSubject().getId())) {
                MessageChain msg = msgEvent.getMessage();
                StringBuilder sb = new StringBuilder();
                for (SingleMessage i:msg){
                    if(i instanceof PlainText a){
                        sb.append(a.getContent());
                    }else if(i instanceof At a){
                        if(!(a.getTarget()==msgEvent.getBot().getId())) {
                            sb.append("@" + msgEvent.getSubject().get(a.getTarget()).getNick());
                    }
                    }else if(i instanceof Image a){
                        sb.append("~`+=:img:"+a.serializeToMiraiCode()+"~`+=");
                    }else if(i instanceof Face a){
                        sb.append("["+a.getName()+"]");
                    }else if(i instanceof QuoteReply a){
                        sb.append("回复" + a.getSource().contentToString()+":\n");
                    }else if(i instanceof AtAll a){
                        sb.append("@所有人");
                    }else {
                        sb.append(i.contentToString());
                    }
                }
                String result = GPT.getReply(msgEvent.getSubject().getId(), sb.toString());
                MessageChain chain = new MessageChainBuilder()
                        .append(result)
                        .append(new At(msgEvent.getSender().getId()))
                        .build();
                msgEvent.getGroup().sendMessage(chain);
                Logger logger = Logger.getDeclaredLogger();
                logger.info("Handled chatting event from Group: %s", msgEvent.getGroup().getId());
            } else {
                msgEvent.getGroup().sendMessage("功能未开启");
            }
        }

    }
    @EventHandler
    public void privateTalk(FriendMessageEvent msgEvent) throws IOException, LoggerNotDeclaredException {
        if (!(msgEvent.getMessage().contentToString().startsWith("/") || msgEvent.getMessage().contentToString().startsWith("绘图 ") || msgEvent.getMessage().contentToString().startsWith("查服 ")|| msgEvent.getMessage().contentToString().startsWith("帮助 ")|| msgEvent.getMessage().contentToString().startsWith("帮助")||msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle")||msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().startsWith("wordle ")|| msgEvent.getMessage().contentToString().matches("[a-zA-Z]+"))) {
            if (FeatureInUse.isInUse("Reply", msgEvent.getSubject().getId())) {
                MessageChain msg = msgEvent.getMessage();
                StringBuilder sb = new StringBuilder();
                for (SingleMessage i:msg){
                    if(i instanceof PlainText a){
                        sb.append(a.getContent());
                    }else if(i instanceof At a){
                        sb.append("@"+a.getTarget()+"@");
                    }else if(i instanceof Image a){
                        sb.append("~`+=:img:"+a.serializeToMiraiCode()+"~`+=");
                    }else if(i instanceof Face a){
                        sb.append("["+a.getName()+"]");
                    }else if(i instanceof QuoteReply a){
                        sb.append("回复" + a.getSource().contentToString()+":\n");
                    }else if(i instanceof AtAll a){
                        sb.append("@所有人@");
                    }else {
                        sb.append(i.contentToString());
                    }
                }
                String result = GPT.getReply(msgEvent.getSubject().getId(), sb.toString());
                msgEvent.getSubject().sendMessage(result);
                Logger logger = Logger.getDeclaredLogger();
                logger.info("Handled message reply at" + msgEvent.getSubject().getId());
            } else {
                msgEvent.getSubject().sendMessage("功能未开启");
            }
        }

    }





}
