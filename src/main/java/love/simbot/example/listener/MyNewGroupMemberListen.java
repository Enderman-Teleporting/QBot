package love.simbot.example.listener;

import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.*;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.BotInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.*;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import love.forte.simbot.filter.MatchType;
import love.simbot.example.tools.Log_settler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static love.simbot.example.tools.properties_settler.read;
import static org.apache.commons.lang.StringUtils.length;


@Beans
public class MyNewGroupMemberListen {
    @Depend
    private MessageContentBuilderFactory messageBuilderFactory;
    private static final Map<String, String> REQUEST_TEXT_MAP = new ConcurrentHashMap<>();
    @OnGroupAddRequest

    public void onRequest(GroupAddRequest groupAddRequest, Setter setter, Sender sender) throws IOException {
        AccountInfo accountInfo = groupAddRequest.getRequestAccountInfo();
        BotInfo botInfo = groupAddRequest.getBotInfo();
        if (!accountInfo.getAccountCode().equals(botInfo.getBotCode())) {
            String text = groupAddRequest.getText();
            if (text != null) {
                REQUEST_TEXT_MAP.put(accountInfo.getAccountCode(), text);
            }
            GroupInfo groupInfo = groupAddRequest.getGroupInfo();

            Log_settler.writelog(accountInfo.getAccountNickname()+"("+accountInfo.getAccountCode()+")"+"申请加入群"+groupInfo.getGroupName()+"("+groupInfo.getGroupCode()+")"+",申请备注："+text+"\n\n\n");
            setter.acceptGroupAddRequest(groupAddRequest.getFlag());
            MessageContentBuilder buil_=messageBuilderFactory.getMessageContentBuilder();
            MessageContent msgg=buil_
                    .text(accountInfo.getAccountNickname())
                    .text("(")
                    .text(accountInfo.getAccountCode())
                    .text(")")
                    .text("申请加入此群,加群信息为")
                    .text(text)
                    .build();
            sender.sendGroupMsg(groupAddRequest.getGroupInfo(),msgg);
        }
    }
 
    @OnGroupMemberIncrease
    public void newGroupMember(GroupMemberIncrease groupMemberIncrease, Sender sender) throws IOException {

        MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();
        AccountInfo accountInfo = groupMemberIncrease.getAccountInfo();
        MessageContent msg = builder
                .at(accountInfo)
                .text(" 欢迎萌新!我是机器人"+groupMemberIncrease.getBotInfo().getBotName()+" 很高兴见到你\n")
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
            Log_settler.writelog("OnGroup"+ groupInfoC);
            Log_settler.writelog(String.valueOf(groupMemberPermissionChanged.getBotInfo()));
            Log_settler.writelog(message_ +"\n\n\n");
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
        Log_settler.writelog("OnGroup"+ groupInfoGMC);
        Log_settler.writelog(String.valueOf(groupNameChanged.getBotInfo()));
        Log_settler.writelog(message1 +"\n\n\n");
    }

    @OnGroup
    public void groupban(GroupMsg groupMsg,Setter setter,Sender sender) throws IOException {
        if (length(groupMsg.getText())>=300&&read("./cache/properties/"+groupMsg.getBotInfo().getBotCode()+".properties","300WordsBan").equals("true")){
            setter.setGroupBan(groupMsg.getGroupInfo(), groupMsg.getAccountInfo(), 300);
            MessageContentBuilder builder3=messageBuilderFactory.getMessageContentBuilder();
            MessageContent message=builder3
                    .at(groupMsg.getAccountInfo())
                    .text("发言过长,禁言五分钟,下次注意")
                    .build();
            sender.sendGroupMsg(groupMsg.getGroupInfo(),message);
            Log_settler.writelog("OnGroup"+ groupMsg.getGroupInfo());
            Log_settler.writelog(String.valueOf(groupMsg.getBotInfo()));
            Log_settler.writelog(message +"\n\n\n");
        }
    }

    @OnGroup
    @Filter(value="群名称 ",matchType = MatchType.STARTS_WITH)
    public void groupName(GroupMsg groupMsg,Setter setter,Sender sender) throws IOException {
        if (read("./cache/properties/"+groupMsg.getBotInfo().getBotCode()+".properties","allowGroupNameChanging").equals("true")) {
            String msg = groupMsg.getText().replace("群名称 ", "");
            setter.setGroupName(groupMsg.getGroupInfo(),msg);
            MessageContentBuilder builder4=messageBuilderFactory.getMessageContentBuilder();
            MessageContent message=builder4
                    .text("已按")
                    .at(groupMsg.getAccountInfo())
                    .text("的建议修改群名")
                    .build();
            sender.sendGroupMsg(groupMsg.getGroupInfo(),message);
            Log_settler.writelog("OnGroup"+ groupMsg.getGroupInfo());
            Log_settler.writelog(String.valueOf(groupMsg.getBotInfo()));
            Log_settler.writelog(message +"\n\n\n");
        }
    }
    @OnGroup
    @Filter(value = "头衔 ",matchType = MatchType.STARTS_WITH)
    public void title(GroupMsg groupMsg,Setter setter,Sender sender) throws IOException{
        if (read("./cache/properties/"+groupMsg.getBotInfo().getBotCode()+".properties","allowChangingTitle").equals("true")) {
            String msg = groupMsg.getText().replace("头衔 ", "");
            setter.setGroupMemberSpecialTitle(groupMsg.getGroupInfo(), groupMsg.getAccountInfo(), msg);
            sender.sendGroupMsg(groupMsg.getGroupInfo(), "已给予头衔");
            Log_settler.writelog("OnGroup" + groupMsg.getGroupInfo());
            Log_settler.writelog(String.valueOf(groupMsg.getBotInfo()));
            Log_settler.writelog("已给予头衔" + "\n\n\n");
        }
    }

    @OnGroup
    @Filter(value = "我要管理",matchType = MatchType.EQUALS)
    public void Admin(GroupMsg groupMsg,Setter setter,Sender sender) throws IOException, InterruptedException {
        if (read("./cache/properties/"+groupMsg.getBotInfo().getBotCode()+".properties","allowGroupNameChanging").equals("true")) {
            setter.setGroupAdmin(groupMsg.getGroupInfo(),groupMsg.getAccountInfo(),true);
            sender.sendGroupMsg(groupMsg.getGroupInfo(), "已给予权限");
            Log_settler.writelog("OnGroup" + groupMsg.getGroupInfo());
            Log_settler.writelog(String.valueOf(groupMsg.getBotInfo()));
            Log_settler.writelog("已给予权限" + "\n\n\n");
            Thread.sleep(60000);
            setter.setGroupAdmin(groupMsg.getGroupInfo(),groupMsg.getAccountInfo(),true);
            MessageContentBuilder builder=messageBuilderFactory.getMessageContentBuilder();
            MessageContent message=builder
                    .text("时间已到,收回")
                    .at(groupMsg.getAccountInfo())
                    .text("的权限")
                    .build();
            sender.sendGroupMsg(groupMsg.getGroupInfo(), message);
            setter.setGroupAdmin(groupMsg.getGroupInfo(),groupMsg.getAccountInfo(),false);
            Log_settler.writelog("OnGroup" + groupMsg.getGroupInfo());
            Log_settler.writelog(String.valueOf(groupMsg.getBotInfo()));
            Log_settler.writelog(message + "\n\n\n");
        }
    }
    @OnGroup
    public void settle(GroupMsg groupMsg){
        (new File("./cache/properties")).mkdirs();
        String fileName = "./cache/properties/"+ groupMsg.getBotInfo().getBotCode()+".properties";
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                f.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
                bw.write("Pic=true\n300WordsBan=true\nallowGroupNameChanging=true\nallowChangeTitle=true\nallowGettingAdmin=false\n");
                bw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

