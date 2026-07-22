package io.github.et.messager;

import io.github.et.Main;
import io.github.et.conopt4j.threading.command.Command;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;

public class ChangeConfigListener extends SimpleListenerHost {
    @EventHandler
    public void config(FriendMessageEvent msg){
        if (msg.getSender().getId() == Main.JSON_NO_GUIDE.getJSONObject("Global").getLong("owner")&&msg.getMessage().contentToString().startsWith("/")) {
            Command.runCommand(msg.getMessage().contentToString().substring(1)).thenAccept(a->{
                msg.getSender().sendMessage(a);
            });
        }

    }
}
