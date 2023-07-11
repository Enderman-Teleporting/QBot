package io.github.et.messager;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

public class Repeater {
    public static void runRepeat(Bot bot){
        for(Group group: bot.getGroups()){
            Runnable runnable=new Runnable(){
                private MessageChain msg1=null,msg2=null;
                @Override
                public void run(){
                    while(true){
                        if (msg1==null){
                            Listener<MessageEvent> listener1=bot.getEventChannel()
                                    .filter(botEvent -> botEvent instanceof MessageEvent && ((MessageEvent) botEvent).getSubject().equals(group))
                                    .subscribeAlways(MessageEvent.class,msg->{
                                        msg1=msg.getMessage();
                                    });
                            listener1.complete();
                        }


                        Listener<MessageEvent> listener2=bot.getEventChannel()
                                .filter(botEvent -> botEvent instanceof MessageEvent && ((MessageEvent) botEvent).getSubject().equals(group))
                                .subscribeAlways(MessageEvent.class,msg->{
                                    msg2=msg.getMessage();
                                    if (msg2.equals(msg1)){
                                        group.sendMessage(msg1);
                                        msg1=null;
                                        msg2=null;
                                    } else{
                                        msg1=msg2;
                                        msg2=null;
                                    }
                                });
                        listener2.complete();
                    }

                }

            };
            Thread t=new Thread(runnable);
            t.start();
        }
    }
}
