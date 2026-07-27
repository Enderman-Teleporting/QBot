package io.github.et.messager;

import io.github.et.conopt4j.logger.Logger;
import io.github.et.tools.ServerSearching;
import io.github.et.utils.json.FeatureInUse;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@SuppressWarnings("unused")
public class ServerSearcher extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Logger.error("Exception occurred when handling a server searching operation");
    }

    @EventHandler
    public void search(MessageEvent msgEvent) throws IOException {
        if (msgEvent.getMessage().contentToString().startsWith("查服 ")) {
            if (FeatureInUse.isInUse("MineServerStat", msgEvent.getSubject().getId())) {
                String msg = msgEvent.getMessage().contentToString();
                msg = msg.substring(3);
                if (msg.contains(":")) {
                    String[] list = msg.split(":");
                    msgEvent.getSubject().sendMessage(ServerSearching.search(list[0], Integer.parseInt(list[1]), msgEvent.getSubject()));
                } else {
                    msgEvent.getSubject().sendMessage(ServerSearching.search(msg, 25565, msgEvent.getSubject()));
                }
                Logger.info("Handled a server searching request");
            } else {
                msgEvent.getSubject().sendMessage("功能未开启");
            }
        }
    }
}
