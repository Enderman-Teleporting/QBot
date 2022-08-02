package love.simbot.example.listener;

import catcode.CatCodeUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
import love.forte.simbot.api.sender.Sender;

import org.json.JSONObject;
@Beans
public class MyPrivateListen {
    @Listen(PrivateMsgRecall.class)
    public void message(PrivateMsgRecall privateMsgRecall, Sender sender){
        sender.sendPrivateMsg(privateMsgRecall, "快说,撤回了什么?!");
    }
    
}
