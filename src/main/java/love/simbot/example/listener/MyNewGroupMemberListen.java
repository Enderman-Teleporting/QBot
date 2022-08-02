package love.simbot.example.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnGroupMemberIncrease;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMemberIncrease;
import love.forte.simbot.api.message.events.GroupMemberPermissionChanged;
import love.forte.simbot.api.message.events.GroupNameChanged;
import love.forte.simbot.api.sender.Sender;


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

    @Listen(GroupMemberPermissionChanged.class)
    public void permissionChange(GroupMemberPermissionChanged groupMemberPermissionChanged, Sender sender){
        AccountInfo accountInfoC=groupMemberPermissionChanged.getAccountInfo();
        AccountInfo operatorinfo=groupMemberPermissionChanged.getOperatorInfo();
        GroupInfo groupInfoC=groupMemberPermissionChanged.getGroupInfo();
        MessageContentBuilder builder1 = messageBuilderFactory.getMessageContentBuilder();
        boolean permissionlose=groupMemberPermissionChanged.isLostManagementRights();
        boolean permissionget=groupMemberPermissionChanged.isGetManagementRights();
        MessageContent message_;
        if (permissionlose){
            message_=builder1
                .at(accountInfoC)
                .text("的权限被")
                .at(operatorinfo)
                .text("失去管理权限")
                .build();
            sender.sendGroupMsg(groupInfoC, message_);
        }
        if (permissionget){
            message_=builder1
                .at(accountInfoC)
                .text("的权限被")
                .at(operatorinfo)
                .text("获得管理权限")
                .build();
            sender.sendGroupMsg(groupInfoC, message_);
        }
        
    }
    @Listen(GroupNameChanged.class)
    public void groupnamechange(GroupNameChanged groupNameChanged, Sender sender){
        AccountInfo accountInfoGMC=groupNameChanged.getOperatorInfo();
        GroupInfo groupInfoGMC=groupNameChanged.getGroupInfo();
        String groupname=groupNameChanged.getAfterChange();
        String groupnamebef=groupNameChanged.getBeforeChange();
        MessageContentBuilder builder2=messageBuilderFactory.getMessageContentBuilder();
        MessageContent message1=builder2
            .at(accountInfoGMC)
            .text("将群名称由")
            .text(groupnamebef)
            .text("改为")
            .text(groupname)
            .build();
        sender.sendGroupMsg(groupInfoGMC,message1);
    }


}

