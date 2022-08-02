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
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.filter.MatchType;
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
    @Filter (value="Steve",matchType = MatchType.EQUALS)
    public void beCalled(GroupMsg groupmsg,Sender sender){
        GroupInfo groupInfo1=groupmsg.getGroupInfo();
        sender.sendGroupMsg(groupInfo1,"┗|｀O′|┛ 嗷~~ 史蒂夫搁这儿呢");
    }
}
