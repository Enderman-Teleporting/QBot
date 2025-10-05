package io.github.et.messager;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.github.et.Main;
import io.github.et.tools.GPT;
import io.github.et.utils.json.FeatureInUse;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.et.tools.GPT.context;

@SuppressWarnings("unused")
public class FreeTalk extends SimpleListenerHost {
    private static Random random=new Random();
    private static ConcurrentHashMap<Long,Integer> targetMessageNum=new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Long,Integer> currentMessageNum=new ConcurrentHashMap<>();
    

    public static String removeThink(String ori) {
        if (ori == null || (!ori.contains("<think>")&&!ori.contains("</think>"))) {
            return ori;
        }
        StringBuilder result = new StringBuilder();
        int i = 0;
        int len = ori.length();
        
        while (i < len) {
            int thinkStart = ori.indexOf("<think>", i);
            if (thinkStart == -1) {
                result.append(ori.substring(i));
                break;
            }
            result.append(ori.substring(i, thinkStart));
            int thinkEnd = ori.indexOf("</think>", thinkStart);
            if (thinkEnd == -1) {
                result.append(ori.substring(thinkStart));
                break;
            }
            i = thinkEnd + 8;
        }
        
        return result.toString();
    }
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when handling free talk operation, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        //throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent",exception);
    }
    @EventHandler
    public void contextRecorder(GroupMessageEvent event) throws InterruptedException, LoggerNotDeclaredException {
        if(FeatureInUse.isInUse("FreeTalk",event.getSubject().getId(),"Reply")) {
            String prompt = "现在你是一名QQ群里的成员,群名称为:"+event.getSubject().getName()+",你的昵称为:" + event.getBot().getNick() + ",你要尽量以网民的口吻聊天,可适当玩梗、玩抽象，你的每一个回答都应控制在20-30字以内,末尾不加句号。在现实生活中，你的设定是这样的：\n" +
                    Main.JSON_NO_GUIDE.getJSONObject("Reply").getJSONObject("FreeTalk").getString("role") + "\n" +
                    "我会以这样的形式向你展示群聊上下文:\n" +
                    "`发送时间` `昵称`(`QQ号`):`消息内容`\n" +
                    "你需要根据这个上下文进行回复,也可以在适宜的位置开启新话题,请直接输入你需要的的回复即可,在需要at某个群成员的地方,请以如下格式at:\n" +
                    "@`QQ号`@\n";
            long id=event.getSubject().getId();
            Calendar date = Calendar.getInstance();
            date.setTime(new Date(System.currentTimeMillis()));
            int hour = date.get(Calendar.HOUR_OF_DAY);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (hour < 22 && hour > 6) {
                if ((!context.containsKey(event.getSubject().getId()))|| context.get(id).isEmpty()) {
                    context.put(id,new ArrayList<>());
                    JSONObject j=new JSONObject();
                    j.put("role", "user");
                    j.put("content", prompt);
                    context.get(id).add(j);
                    targetMessageNum.put(id, 0);
                    currentMessageNum.put(id, 0);
                }
                if (targetMessageNum.get(id) == 0) {
                    targetMessageNum.put(id, random.nextInt(5, 11));
                }
                JSONObject j=new JSONObject();
                MessageChain msg = event.getMessage();
                StringBuilder sb = new StringBuilder();
                sb.append(sdf.format(new Date())).append(" ").append(event.getSender().getNick()).append("(").append(event.getSender().getId()).append("):");
                j.put("role", "user");
                JSONArray ja= new JSONArray();
                for (SingleMessage i:msg){
                    if(i instanceof PlainText a){
                        sb.append(a.getContent());
                    }else if(i instanceof At a){
                        sb.append("@"+a.getTarget()+"@");
                    }else if(i instanceof Image a){
                        JSONObject jo=new JSONObject();
                        sb.append("[图片]");
                        jo.put("type", "image_url");
                        JSONObject temp = new JSONObject();
                        String code=a.serializeToMiraiCode();
                        temp.put("url", code.substring(13, code.length() - 1));
                        jo.put("image_url", temp);
                        ja.add(jo);
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
                if(!ja.isEmpty()){
                    JSONObject jo = new JSONObject();
                    jo.put("type", "text");
                    jo.put("text", sb.toString());
                    ja.add(jo);
                    j.put("content", ja);
                }else {
                    j.put("content", sb.toString());
                }
                context.get(id).add(j);
                currentMessageNum.put(id,currentMessageNum.get(id)+1);
                if(context.get(id).size()>Main.JSON_NO_GUIDE.getJSONObject("Reply").getInteger("Max_Message_Count")+1){
                    context.get(id).remove(1);
                }
                if (Objects.equals(currentMessageNum.get(id), targetMessageNum.get(id))) {
                    String content = GPT.freeSpeech(id);
                    if (content != null) {
                        content = content.replaceAll("^((`\\d+:\\d+`\\s*:*\\s*[^\\n\\s()]+\\s*\\(\\d+\\)\\s*:*\\s*)|(.+\\d-\\d-\\d\\s*.*\\d\\s*:\\s*\\d\\s*"+event.getBot().getNick()+"\\s*))?", "").trim();
                        String[] messages = content.split("[\n，。；：,.;:` ]");
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

                        Thread.sleep(150);
                        for (String i : messages) {
                            Thread.sleep(300L * i.length());
                            if(i.matches("[^()]+\\(\\d+\\)\\s*（*）*")){
                                int m=Integer.parseInt(i.substring(i.indexOf("(")+1,i.indexOf(")")));
                                event.getSubject().sendMessage(new At(m));
                                continue;
                            }
                            String[] a=i.split("@");
                            MessageChainBuilder msgBuilder=new MessageChainBuilder();
                            for(String k:a){
                                if(k.isEmpty()){
                                    continue;
                                }
                                if(k.matches("[0-9]+")){
                                    try {
                                        msgBuilder.add(new At(Long.parseLong(k)));
                                    }catch (Exception ignored){}
                                }else{
                                    msgBuilder.add(new PlainText(k));
                                }

                            }
                            MessageChain messages1=msgBuilder.build();
                            event.getSubject().sendMessage(messages1);

                        }
                        targetMessageNum.put(id, 0);
                        currentMessageNum.put(id, 0);
                    } else {
                        Logger logger = Logger.getDeclaredLogger();
                        logger.error("Failed to send message in \"Free Talk\" mode");
                        context.get(id).clear();
                        targetMessageNum.put(id, 0);
                        currentMessageNum.put(id, 0);
                    }
                }
            } else {
                context.put(event.getSubject().getId(), new ArrayList<>());
                targetMessageNum.put(event.getSubject().getId(), 0);
                currentMessageNum.put(event.getSubject().getId(), 0);
            }
        }
    }

}
