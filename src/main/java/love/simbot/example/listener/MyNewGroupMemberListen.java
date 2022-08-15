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
import love.simbot.example.Log_settler;
import java.io.IOException;



@Beans
public class MyNewGroupMemberListen {
    @Depend
    private MessageContentBuilderFactory messageBuilderFactory;
 
    @OnGroupMemberIncrease
    public void newGroupMember(GroupMemberIncrease groupMemberIncrease, Sender sender) throws IOException {
        MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();
        AccountInfo accountInfo = groupMemberIncrease.getAccountInfo();
        MessageContent msg = builder
                .at(accountInfo)
                .text(" 欢迎萌新!我是机器人菲菲 很高兴见到你\n")
                .build();
        GroupInfo groupInfo = groupMemberIncrease.getGroupInfo();
        sender.sendGroupMsg(groupInfo, msg);
        Log_settler.writelog("OnGroup"+String.valueOf(groupInfo));
        Log_settler.writelog(String.valueOf(groupMemberIncrease.getBotInfo()));
        Log_settler.writelog(String.valueOf(msg)+"\n\n\n");
    }

    @Listen(GroupMemberPermissionChanged.class)
    public void permissionChange(GroupMemberPermissionChanged groupMemberPermissionChanged, Sender sender) throws IOException {
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
                .text("降级为群成员权限")
                .build();
            sender.sendGroupMsg(groupInfoC, message_);
            Log_settler.writelog("OnGroup"+String.valueOf(groupInfoC));
            Log_settler.writelog(String.valueOf(groupMemberPermissionChanged.getBotInfo()));
            Log_settler.writelog(String.valueOf(message_)+"\n\n\n");
        }
        if (permissionget){
            message_=builder1
                .at(accountInfoC)
                .text("的权限被")
                .at(operatorinfo)
                .text("升级为管理权限")
                .build();
            sender.sendGroupMsg(groupInfoC, message_);
            Log_settler.writelog("OnGroup"+String.valueOf(groupInfoC));
            Log_settler.writelog(String.valueOf(groupMemberPermissionChanged.getBotInfo()));
            Log_settler.writelog(String.valueOf(message_)+"\n\n\n");
        }
        
    }
    @Listen(GroupNameChanged.class)
    public void groupnamechange(GroupNameChanged groupNameChanged, Sender sender) throws IOException {
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
        Log_settler.writelog("OnGroup"+String.valueOf(groupInfoGMC));
        Log_settler.writelog(String.valueOf(groupNameChanged.getBotInfo()));
        Log_settler.writelog(String.valueOf(message1)+"\n\n\n");
    }


}

