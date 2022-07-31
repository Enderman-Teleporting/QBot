package love.simbot.example.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnGroupAddRequest;
import love.forte.simbot.annotation.OnGroupMemberIncrease;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.Reply;
import love.forte.simbot.api.message.assists.Flag;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupAddRequest;
import love.forte.simbot.api.message.events.GroupMemberIncrease;
import love.forte.simbot.api.message.events.ReduceEventGet;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Beans
public class MyNewGroupMemberListen {
    @Depend
    private MessageContentBuilderFactory messageBuilderFactory;
 
    @OnGroupMemberIncrease
    public void newGroupMember(GroupMemberIncrease groupMemberIncrease, Sender sender) {
        MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();
        AccountInfo accountInfo = groupMemberIncrease.getAccountInfo();
        MessageContent msg = builder
                .at(accountInfo)
                .text(" 欢迎萌新!我是机器人Steve 很高兴见到你\n")
                .build();
        GroupInfo groupInfo = groupMemberIncrease.getGroupInfo();
        sender.sendGroupMsg(groupInfo, msg);
    }

    @Listen(ReduceEventGet.class)
    public void GroupMemberReduction(ReduceEventGet reduceEventGet, Sender sender){
        AccountInfo accountinfored= reduceEventGet.getAccountInfo();
        sender.sendGroupMsg(reduceEventGet.getGroupInfo, accountinfored+"离开了我们");
    }


}

