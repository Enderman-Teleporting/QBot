package love.simbot.example.listener;

import catcode.CatCodeUtil;
import catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.GroupMsgRecall;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import love.simbot.example.tools.Log_settler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static love.simbot.example.tools.API.getApi;
import static love.simbot.example.tools.properties_settler.read;
import static love.simbot.example.tools.serverSearching.search;

@Beans
public class MyGroupListen {
    HashMap<String, String> renyunyiren= new HashMap<String, String>();
    @Depend
    private MessageContentBuilderFactory messageBuilderFactory;

    @OnGroup
    public void onGroupMsg(GroupMsg groupMsg) throws IOException {
        Log_settler.writelog(groupMsg.getText());
        Log_settler.writelog(groupMsg.getMsg());
        MessageContent msgContent = groupMsg.getMsgContent();
        Log_settler.writelog(String.valueOf(msgContent));
        List<Neko> imageCats = msgContent.getCats("image");
        Log_settler.writelog("img counts: " + imageCats.size());
        for (Neko image : imageCats) {
            Log_settler.writelog("Img url: " + image.get("url"));
        }
        GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
        Log_settler.writelog("OnGroup\n"+accountInfo.getAccountCode());
        Log_settler.writelog(accountInfo.getAccountNickname());
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        Log_settler.writelog(String.valueOf(groupMsg.getGroupInfo()));
        Log_settler.writelog(groupInfo.getGroupCode());
        Log_settler.writelog(groupInfo.getGroupName());
    }
    @Listen(GroupMsg.class)
    public void beCalled(GroupMsg groupmsg,Sender sender) throws IOException {
        if (groupmsg.getText().equals(groupmsg.getBotInfo().getAccountNickname())){
            GroupInfo groupInfo1 = groupmsg.getGroupInfo();
            sender.sendGroupMsg(groupInfo1, "是谁在叫" + groupmsg.getBotInfo().getAccountNickname() + "啊");
            Log_settler.writelog("OnGroup" + String.valueOf(groupmsg.getBotInfo()));
            Log_settler.writelog(String.valueOf(groupInfo1));
            Log_settler.writelog("bot:是谁在叫" + groupmsg.getBotInfo().getAccountNickname() + "呀");
        }
    }

    @Listen(GroupMsg.class)
    @Filter(atBot=true,matchType=MatchType.CONTAINS)
    public void Groupmsglisten(GroupMsg groupMsg, MsgGet msgGet, Sender sender) throws IOException, InterruptedException{
        GroupInfo listenedgroupinfo=groupMsg.getGroupInfo();
        AccountInfo accountInfoabc=groupMsg.getAccountInfo();
        String gottenmsg1=groupMsg.getText();
        gottenmsg1=gottenmsg1.replace(" ","%20");
        String result =getApi("http://api.qingyunke.com/api.php?key=free&appid=0&msg="+gottenmsg1,"content");
        result=result.replace("菲菲",groupMsg.getBotInfo().getBotName());
        if (result.contains("{")&&result.contains("}")) {
            String prev = "", aft = "", num = "";
            char[] MsgArray = result.toCharArray();
            for (int i = 0; MsgArray[i] != '{'; i++) {
                prev += MsgArray[i];
            }
            for (int j = 1; j < MsgArray.length; j++) {
                if (MsgArray[j - 1] == ':') {
                    for (int k = j; MsgArray[k + 1] != '}'; k++) {
                        num += MsgArray[k];
                    }
                }
                if (MsgArray[j - 1] == '}') {
                    for (int l = j; l < MsgArray.length; l++) {
                        aft += MsgArray[l];
                    }
                }
            }
            MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();
            MessageContent message = builder
                    .text(prev)
                    .face(Integer.parseInt(num))
                    .text(aft)
                    .at(accountInfoabc)
                    .build();
            sender.sendGroupMsg(listenedgroupinfo, message);
            Log_settler.writelog("OnGroup" + String.valueOf(groupMsg.getBotInfo()));
            Log_settler.writelog("bot:" + message);
            Log_settler.writelog(String.valueOf(listenedgroupinfo));
        }
        else{
            MessageContentBuilder builder = messageBuilderFactory.getMessageContentBuilder();
            MessageContent content = builder
                    .text(result)
                    .at(accountInfoabc)
                    .build();
            sender.sendGroupMsg(listenedgroupinfo, content);
            Log_settler.writelog("OnGroup" + String.valueOf(groupMsg.getBotInfo()));
            Log_settler.writelog("bot:"+content);
            Log_settler.writelog(String.valueOf(listenedgroupinfo));
        }
        Thread.sleep(3000);
    }
    @Listen(GroupMsgRecall.class)
    public void message(GroupMsgRecall groupMsgRecall, Sender sender) throws IOException {
    	GroupInfo groupinforec=groupMsgRecall.getGroupInfo();
        sender.sendGroupMsg(groupinforec, "快说,撤回了什么?!");
        Log_settler.writelog("OnGroup"+String.valueOf(groupMsgRecall.getBotInfo()));
        Log_settler.writelog("bot:"+"快说,撤回了什么?!");
        Log_settler.writelog(String.valueOf(groupinforec));
    }

    @Listen(GroupMsg.class)
    public void renyunyiyun(GroupMsg groupMsg, Sender sender){
        if (renyunyiren.isEmpty()){
            renyunyiren.put("a",groupMsg.getMsg());
        }
        else{
            renyunyiren.put("b",groupMsg.getMsg());
        }
        if (renyunyiren.size()==2 && renyunyiren.get("a").equals(renyunyiren.get("b"))){
            sender.sendGroupMsg(groupMsg.getGroupInfo(),renyunyiren.get("b"));
            renyunyiren.clear();
        }
        else{
            if(renyunyiren.size()==2) {
                renyunyiren.clear();
                renyunyiren.put("a", groupMsg.getMsg());
            }
        }
    }

    @Listen(GroupMsg.class)
    @Filter(value = "二次元",matchType=MatchType.EQUALS)
    public void pic(GroupMsg groupMsg, Sender sender) throws IOException {
        if (read("./cache/properties/"+groupMsg.getBotInfo().getBotCode()+".properties","Pic").equals("true")) {
            GroupInfo groupInfo = groupMsg.getGroupInfo();
            final CatCodeUtil catUtil = CatCodeUtil.INSTANCE;
            String img = catUtil.toCat("image", true, "url=https://www.dmoe.cc/random.php");
            sender.sendGroupMsg(groupInfo, img);
            Log_settler.writelog("OnGroup");
            Log_settler.writelog("bot:"+img);
        }
    }
    @Listen(GroupMsg.class)
    @Filter(value = "MC好图",matchType=MatchType.EQUALS)
    public void mc(GroupMsg groupMsg, Sender sender) throws IOException {
        if (read("./cache/properties/"+groupMsg.getBotInfo().getBotCode()+".properties","MCPic").equals("true")) {
            GroupInfo groupInfo = groupMsg.getGroupInfo();
            final CatCodeUtil catUtil = CatCodeUtil.INSTANCE;
            String img = catUtil.toCat("image", true, "url=https://api.lantianyun.tk");
            sender.sendGroupMsg(groupInfo, img);
            Log_settler.writelog("OnGroup");
            Log_settler.writelog("bot:"+img);
        }
    }
    @OnGroup
    @Filter(value = "查服 ",matchType = MatchType.STARTS_WITH)
    public void MCServerStat (GroupMsg groupMsg, Sender sender) throws IOException {
        if(read("./cache/properties/"+groupMsg.getBotInfo().getBotCode()+".properties","MCServerStat").equals("true")) {
            final var info = groupMsg.getGroupInfo();
            var content = groupMsg.getText().replace("查服 ", "");
            String[] contents;
            if (content.contains(":")) {
                try {
                    contents = content.split(":");
                    sender.sendGroupMsg(info, search(contents[0], Integer.parseInt(contents[1])));
                    Log_settler.writelog("OnGroup" + String.valueOf(groupMsg.getBotInfo()));
                    Log_settler.writelog("bot:" + search(contents[0], Integer.parseInt(contents[1])));
                    Log_settler.writelog(String.valueOf(info));
                } catch (Exception e) {
                    sender.sendGroupMsg(info, "yee~,你是不是输错了");
                    Log_settler.writelog("OnGroup" + String.valueOf(groupMsg.getBotInfo()));
                    Log_settler.writelog("bot:" + "yee~,你是不是输错了");
                    Log_settler.writelog(String.valueOf(info));
                }
            } else if (content.contains("：")) {
                sender.sendGroupMsg(info, "冒号是英文冒号哦");
                Log_settler.writelog("OnGroup" + String.valueOf(groupMsg.getBotInfo()));
                Log_settler.writelog("bot:" + "冒号是英文冒号哦");
                Log_settler.writelog(String.valueOf(info));
            } else {
                sender.sendGroupMsg(info, search(content));
                Log_settler.writelog("OnGroup" + String.valueOf(groupMsg.getBotInfo()));
                Log_settler.writelog("bot:" + search(content));
                Log_settler.writelog(String.valueOf(info));
            }
        }
    }

}

