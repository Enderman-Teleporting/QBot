package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.tools.Ranking;
import io.github.et.utils.json.FeatureInUse;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MessageCount extends SimpleListenerHost {
    private static HashMap<Long, Ranking> messageCount = new HashMap<>();
    private static String date= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when counting message, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent", exception);
    }

    @EventHandler
    public void count(GroupMessageEvent event) {
        if (FeatureInUse.isInUse("Ranking", event.getSubject().getId())) {
            String dateCurrent = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            if (!dateCurrent.equals(date)) {
                messageCount.remove(event.getSubject().getId());
            }
            if (!messageCount.containsKey(event.getSubject().getId())) {
                messageCount.put(event.getSubject().getId(), new Ranking("消息数排行榜"));
            }
            messageCount.get(event.getSubject().getId()).add(event.getSender().getId(), 1);
        }
    }

    @EventHandler
    public void getRankingList(GroupMessageEvent event){
        if(FeatureInUse.isInUse("Ranking",event.getSubject().getId())) {
            if(event.getMessage().contentToString().equals("消息排名")) {
                if(!messageCount.containsKey(event.getSubject().getId())) {
                    messageCount.put(event.getSubject().getId(), new Ranking("消息数排行榜"));
                }
                event.getSubject().sendMessage(messageCount.get(event.getSubject().getId()).toRankingString(event.getSubject()));
            }
        }
    }
}
