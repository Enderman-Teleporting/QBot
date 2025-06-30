package io.github.et.messager;

import io.github.et.Main;
import io.github.et.tools.GPT;
import io.github.et.utils.json.FeatureInUse;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class FreeTalk extends SimpleListenerHost {
    private static Random random=new Random();
    private static ConcurrentHashMap<Long,ArrayList<String>> context= new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long,Integer> targetMessageNum=new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long,Integer> currentMessageNum=new ConcurrentHashMap<>();
    @EventHandler
    public void contextRecorder(GroupMessageEvent event) throws InterruptedException, LoggerNotDeclaredException {
        if(FeatureInUse.isInUse("FreeTalk",event.getSubject().getId(),"Reply")) {
            Calendar date = Calendar.getInstance();
            date.setTime(new Date(System.currentTimeMillis()));
            int hour = date.get(Calendar.HOUR_OF_DAY);
            if (hour < 22 && hour > 6) {
                if (!context.containsKey(event.getSubject().getId())) {
                    context.put(event.getSubject().getId(), new ArrayList<>());
                    targetMessageNum.put(event.getSubject().getId(), 0);
                    currentMessageNum.put(event.getSubject().getId(), 0);
                }
                if (targetMessageNum.get(event.getSubject().getId()) == 0) {
                    targetMessageNum.put(event.getSubject().getId(), random.nextInt(5, 11));
                }
                add(event.getSender(), event.getSubject().getId(), event.getMessage().contentToString());
                if (Objects.equals(currentMessageNum.get(event.getSubject().getId()), targetMessageNum.get(event.getSubject().getId()))) {
                    String content = GPT.freeSpeech(context.get(event.getSubject().getId()), Main.bot.getNick());
                    if (content != null) {
                        String[] messages = content.replace("，", "|").replace("。", "|").replace("？", "|").replace("！", "|").split("\\|");
                        for (int i = 0; i < messages.length; i++) {
                            messages[i] = messages[i].trim();
                            if (messages[i].isEmpty()) {
                                continue;
                            }
                            switch (random.nextInt(0, 6)) {
                                case 0 -> messages[i] += "（";
                                case 1 -> messages[i] += "（）";
                                default -> messages[i] += "";

                            }
                        }

                        Thread.sleep(1500);
                        for (String i : messages) {
                            Thread.sleep(3000L * i.length());
                            event.getSubject().sendMessage(i);
                            add(event.getSubject().getBotAsMember(), event.getSubject().getId(), i);
                        }
                        targetMessageNum.put(event.getSubject().getId(), 0);
                        currentMessageNum.put(event.getSubject().getId(), 0);
                    } else {
                        Logger logger = Logger.getDeclaredLogger();
                        logger.error("Failed to send message in \"Free Talk\" mode");
                        targetMessageNum.put(event.getSubject().getId(), 0);
                        currentMessageNum.put(event.getSubject().getId(), 0);
                    }
                }
            } else {
                context.put(event.getSubject().getId(), new ArrayList<>());
                targetMessageNum.put(event.getSubject().getId(), 0);
                currentMessageNum.put(event.getSubject().getId(), 0);
            }
        }
    }
    public static void add(Member sender,long group,String message){
        context.get(group).add((sender.getId()==Main.bot.getId()?"你":sender.getNick())+":"+message);
        currentMessageNum.put(group,currentMessageNum.get(group)+1);
        if(context.get(group).size()>16){
            context.get(group).remove(0);
        }
    }
}
