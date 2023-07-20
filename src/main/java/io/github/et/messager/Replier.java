package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Face;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static io.github.et.tools.API.getApi;

@SuppressWarnings("unused")
public class Replier extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when handling a repeating operation, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent",exception);
    }

    @EventHandler
    public void groupTalk(GroupMessageEvent msgEvent) throws IOException, LoggerNotDeclaredException {
        if(msgEvent.getMessage().contains(new At(msgEvent.getBot().getId()))) {
            String msg = msgEvent.getMessage().contentToString();
            msg = msg.replace(" ", "%20");
            String result = getApi("https://api.qingyunke.com/api.php?key=free&appid=0&msg=" + msg, "content");
            result = result.replace("菲菲", msgEvent.getBot().getNick());
            if (result.contains("{") && result.contains("}")) {
                StringBuilder prev = new StringBuilder();
                StringBuilder aft = new StringBuilder();
                StringBuilder num = new StringBuilder();
                char[] MsgArray = result.toCharArray();
                for (int i = 0; MsgArray[i] != '{'; i++) {
                    prev.append(MsgArray[i]);
                }
                for (int j = 1; j < MsgArray.length; j++) {
                    if (MsgArray[j - 1] == ':') {
                        for (int k = j; MsgArray[k + 1] != '}'; k++) {
                            num.append(MsgArray[k]);
                        }
                    }
                    if (MsgArray[j - 1] == '}') {
                        for (int l = j; l < MsgArray.length; l++) {
                            aft.append(MsgArray[l]);
                        }
                    }
                }
                MessageChain chain = new MessageChainBuilder()
                        .append(prev.toString())
                        .append(new Face(Integer.parseInt(num.toString())))
                        .append(aft)
                        .append(new At(msgEvent.getSender().getId()))
                        .build();
                msgEvent.getSubject().sendMessage(chain);
            }else {
                MessageChain chain=new MessageChainBuilder()
                        .append(result)
                        .append(new At(msgEvent.getSender().getId()))
                        .build();
            }
            Logger logger=Logger.getDeclaredLogger();
            logger.info("Handled chatting event from Group: %s",msgEvent.getGroup().getId());
        }

    }
    @EventHandler
    public void privateTalk(FriendMessageEvent msgEvent) throws IOException {
        String msg = msgEvent.getMessage().contentToString();
        msg = msg.replace(" ", "%20");
        String result = getApi("https://api.qingyunke.com/api.php?key=free&appid=0&msg=" + msg, "content");
        result = result.replace("菲菲", msgEvent.getBot().getNick());
        if (result.contains("{") && result.contains("}")) {
            StringBuilder prev = new StringBuilder();
            StringBuilder aft = new StringBuilder();
            StringBuilder num = new StringBuilder();
            char[] MsgArray = result.toCharArray();
            for (int i = 0; MsgArray[i] != '{'; i++) {
                prev.append(MsgArray[i]);
            }
            for (int j = 1; j < MsgArray.length; j++) {
                if (MsgArray[j - 1] == ':') {
                    for (int k = j; MsgArray[k + 1] != '}'; k++) {
                        num.append(MsgArray[k]);
                    }
                }
                if (MsgArray[j - 1] == '}') {
                    for (int l = j; l < MsgArray.length; l++) {
                        aft.append(MsgArray[l]);
                    }
                }
            }
            MessageChain chain = new MessageChainBuilder()
                    .append(prev.toString())
                    .append(new Face(Integer.parseInt(num.toString())))
                    .append(aft)
                    .build();
            msgEvent.getSubject().sendMessage(chain);
        }else {
            MessageChain chain=new MessageChainBuilder()
                    .append(result)
                    .build();
        }
    }

}
