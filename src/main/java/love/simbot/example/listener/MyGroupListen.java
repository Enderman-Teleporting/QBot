package love.simbot.example.listener;

import catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.GroupMsgRecall;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@Beans
public class MyGroupListen {
    @OnGroup
    public void onGroupMsg(GroupMsg groupMsg) {
        System.out.println(groupMsg.getText());
        System.out.println(groupMsg.getMsg());
        MessageContent msgContent = groupMsg.getMsgContent();
        System.out.println(msgContent);
        List<Neko> imageCats = msgContent.getCats("image");
        System.out.println("img counts: " + imageCats.size());
        for (Neko image : imageCats) {
            System.out.println("Img url: " + image.get("url"));
        }
        GroupAccountInfo accountInfo = groupMsg.getAccountInfo();
        System.out.println(accountInfo.getAccountCode());
        System.out.println(accountInfo.getAccountNickname());
        GroupInfo groupInfo = groupMsg.getGroupInfo();
        System.out.println(groupInfo.getGroupCode());
        System.out.println(groupInfo.getGroupName());
    }
    @Listen(GroupMsg.class)
    @Filter (value="菲菲",matchType = MatchType.EQUALS)
    public void beCalled(GroupMsg groupmsg,Sender sender){
        GroupInfo groupInfo1=groupmsg.getGroupInfo();
        sender.sendGroupMsg(groupInfo1,"是谁在叫菲菲啊");
    }

    @Listen(GroupMsg.class)
    @Filter(atBot=true,matchType=MatchType.CONTAINS)
    public void Groupmsglisten(GroupMsg groupMsg, MsgGet msgGet, Sender sender) throws IOException, InterruptedException{
        GroupInfo listenedgroupinfo=groupMsg.getGroupInfo();
        String gottenmsg1=groupMsg.getText();
        URL url=new URL ("http://api.qingyunke.com/api.php?key=free&appid=0&msg="+gottenmsg1);
        InputStream is =url.openStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String result =br.readLine();
        JSONObject obj=JSONObject.fromObject(result);
        result=obj.getString("content");
        sender.sendGroupMsg(listenedgroupinfo, result);
        Thread.sleep(3000);
    }
    @Listen(GroupMsgRecall.class)
    public void message(GroupMsgRecall groupMsgRecall, Sender sender){
    	GroupInfo groupinforec=groupMsgRecall.getGroupInfo();
        sender.sendGroupMsg(groupinforec, "快说,撤回了什么?!");
    }
    
}

