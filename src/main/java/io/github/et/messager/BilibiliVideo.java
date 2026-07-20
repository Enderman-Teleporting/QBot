package io.github.et.messager;

import com.alibaba.fastjson.JSONObject;
import io.github.et.conopt4j.logger.Logger;
import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.utils.bilibili.BilibiliVideoInfoFetcher;
import io.github.et.utils.json.FeatureInUse;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

@SuppressWarnings("unused")
public class BilibiliVideo extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        Logger.error("Exception occurred when fetching some information of a Bilibili video, error info as follows:");
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent", exception);
    }

    @EventHandler
    public void bilibiliVideo(MessageEvent event) throws IOException {
        try {
            if (FeatureInUse.isInUse("Bilibili", event.getSubject().getId())) {
                for (Message i : event.getMessage()) {
                    if (i instanceof LightApp a) {
                        String m = a.getContent();
                        if (!m.contains("b23.tv")) {
                            return;
                        }
                        sth(event, m);
                    } else if (i instanceof PlainText a) {
                        String m = a.getContent();
                        if (m.matches("^(https://|http://|)(www.bilibili.com/video/|b23.tv/).+")) {
                            sth(event, m);
                        }
                    }
                }
            }
        }catch (Exception e){
            event.getSubject().sendMessage(e.getMessage());
        }
    }

    private static void sth(MessageEvent event, String m) throws IOException {
        JSONObject js = BilibiliVideoInfoFetcher.getVideoInfo(m);
        JSONObject js2 = (JSONObject) js.get("data");
        JSONObject js3 = (JSONObject) js2.get("stat");
        JSONObject js4 = (JSONObject) js2.get("owner");
        Image img = ExternalResource.uploadAsImage(new URL(js2.getString("pic")).openStream(), event.getSubject());
        MessageChain reply = new MessageChainBuilder()
                .append(img)
                .append(new PlainText(js2.getString("title") + "(by " + js4.getString("name") + ")\n"))
                .append(new PlainText("点赞：" + js3.getInteger("like") + "\n"))
                .append(new PlainText("投币：" + js3.getInteger("coin") + "\n"))
                .append(new PlainText("收藏：" + js3.getInteger("favorite") + "\n"))
                .append(new PlainText("分享：" + js3.getInteger("share") + "\n"))
                .append(new PlainText("播放量" + js3.getInteger("view") + "\n"))
                .append(new PlainText("弹幕数" + js3.getInteger("danmaku") + "\n"))
                .append(new PlainText("评论数" + js3.getInteger("reply") + "\n"))
                .append(new PlainText("简介"+js2.getString("desc")+"\n"))
                .build();
        event.getSubject().sendMessage(reply);
    }

}