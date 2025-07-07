package io.github.et.messager;

import io.github.et.exceptions.messageExceptions.IllegalMessageDealingException;
import io.github.et.tools.GPT_Image;
import io.github.et.utils.json.FeatureInUse;
import io.github.ettoolset.tools.logger.Logger;
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

@SuppressWarnings("unused")
public class ImageGenerator extends SimpleListenerHost {
    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        try {
            Logger logger = Logger.getDeclaredLogger();
            logger.error("Exception occurred when handling a image generating operation, error info as follows:");
        } catch (LoggerNotDeclaredException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalMessageDealingException("Exception occurred when dealing with MessageEvent",exception);
    }

    @EventHandler
    public void generate(MessageEvent event) throws LoggerNotDeclaredException, IOException {
        if (FeatureInUse.isInUse("Image", event.getSubject().getId(), "Reply")) {
            Logger logger = Logger.getDeclaredLogger();
            if (event.getMessage().contentToString().startsWith("绘图 ")) {
                String prompt = event.getMessage().contentToString().substring(3).replace("\n", "<br/>");
                String image = GPT_Image.generateImage(prompt);
                if (prompt.startsWith("生成图片失败")) {
                    event.getSubject().sendMessage(image);
                } else {
                    ExternalResource ex = ExternalResource.Companion.create(GPT_Image.getUrlByByte(image));
                    Image img = ExternalResource.uploadAsImage(ex, event.getSubject());
                    MessageChain chain = new MessageChainBuilder()
                            .append(img)
                            .build();
                    event.getSubject().sendMessage(chain);
                }
                logger.info("Handled image generating event from Group: %s", event.getSubject().getId());
            }
        }else {
            event.getSubject().sendMessage("功能未开启");
        }
    }
}
