package io.github.et.messager;

import io.github.et.conopt4j.logger.Logger;
import io.github.et.subprocessLoader.ConfigLoader;
import io.github.et.subprocessLoader.MCServer;
import io.github.et.subprocessLoader.ServerStream;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("unused")
public class MinecraftServer extends SimpleListenerHost {
    private static final String[] suffixes=new String[]{"MP4","AVI","MOV","MKV","FLV","WMV","3GP","VID"};
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Logger.error("Exception occurred when linking MC Server");
    }

    @EventHandler
    public void onMessage(GroupMessageEvent evt) throws IOException {
        long id= evt.getSubject().getId();
        for(MCServer ms: ConfigLoader.servers){
            if(ms.getGroup()==id){//TODO 优化？
                StringBuilder msg=new StringBuilder("["+ms.getName()+"]<"+evt.getSender().getNick()+">");
                BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(ServerStream.os, StandardCharsets.UTF_8));
                for (SingleMessage i:evt.getMessage()) {
                    if (i instanceof PlainText a) {
                        msg.append(a.contentToString().replaceAll("\\n", "''_nL_''"));
                    } else if (i instanceof Image b) {
                        msg.append("(pic)[" + b.getImageId() + "]");
                    }else if(i instanceof Face c){
                        msg.append("(face)["+c.getId()+"]");
                    }else if(i instanceof FileMessage d){
                        String ID = "";
                        Pattern pattern = Pattern.compile("url=(https?://[^)]+)");
                        Matcher matcher = pattern.matcher(d.toString());

                        if (matcher.find()) {
                            ID= matcher.group(1);
                        }
                        boolean flag=false;
                        for(String j:suffixes){
                            if(d.getName().toUpperCase().endsWith(j)){
                                msg.append("(vid)["+ID+"]");
                                flag=true;
                                break;
                            }
                        }
                        if(!flag){
                            msg.append("(file)["+d.getName()+"]");
                        }

                    }else if(i instanceof At e){
                        msg.append("@"+evt.getSubject().get(e.getTarget()).getNick());
                    }else if(i instanceof AbstractServiceMessage f){
                        msg.append("(service)["+f.getContent()+"]");
                    } else if (i instanceof Audio g) {
                        msg.append("(audio)");
                    }
                }
                bw.write(msg.toString());
                bw.newLine();
                bw.flush();
                if(evt.getMessage().contentToString().matches("whitelist\\s[a-zA-Z0-9_]+")){
                    evt.getSubject().sendMessage("已将"+evt.getMessage().contentToString().replace("whitelist ","")+"添加到白名单");
                } else if (evt.getMessage().contentToString().startsWith("whitelist ")){
                    evt.getSubject().sendMessage("请将游戏内名称应只包含英文字母,数字和下划线");
                }
            }
        }
    }



}
