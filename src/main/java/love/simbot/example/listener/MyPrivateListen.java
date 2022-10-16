package love.simbot.example.listener;

import catcode.CatCodeUtil;
import catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.*;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.api.sender.Setter;
import love.forte.simbot.filter.MatchType;
import love.simbot.example.tools.Log_settler;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static love.simbot.example.tools.API.getApi;
import static love.simbot.example.tools.properties_settler.read;

@Beans
public class MyPrivateListen {
    private static final Map<String, String> REQUEST_TEXT_MAP = new ConcurrentHashMap<>();
    @OnPrivate
    public void onPrivateMsg(PrivateMsg privateMsg) throws IOException {
        Log_settler.writelog(privateMsg.getText());
        Log_settler.writelog(privateMsg.getMsg());
        MessageContent msgContent = privateMsg.getMsgContent();
        Log_settler.writelog(String.valueOf(msgContent));
        List<Neko> imageCats = msgContent.getCats("image");
        Log_settler.writelog("img counts: " + imageCats.size());
        for (Neko image : imageCats) {
            Log_settler.writelog("Img url: " + image.get("url"));
        }
        AccountInfo accountInfo = (AccountInfo) privateMsg.getAccountInfo();
        Log_settler.writelog("OnPrivate\n"+accountInfo.getAccountCode());
        Log_settler.writelog(accountInfo.getAccountNickname());
        Log_settler.writelog(String.valueOf(privateMsg.getAccountInfo()));
        Log_settler.writelog("\n\n");
    }
    @Listen(PrivateMsgRecall.class)
    public void message(PrivateMsgRecall privateMsgRecall, Sender sender) throws IOException {
        sender.sendPrivateMsg(privateMsgRecall, "快说,撤回了什么?!");
        Log_settler.writelog("OnPrivate"+String.valueOf(privateMsgRecall.getBotInfo()));
        Log_settler.writelog("bot:"+"快说,撤回了什么?!");
        Log_settler.writelog(String.valueOf(privateMsgRecall.getAccountInfo())+"\n\n\n");
    }
    @Listen(PrivateMsg.class)
    public void Privatemsglisten(PrivateMsg privateMsg, MsgGet msgGet, Sender sender) throws IOException, InterruptedException{
        if(!privateMsg.getText().equals("二次元")) {
            AccountInfo listenedinfo = privateMsg.getAccountInfo();
            String gottenmsg2 = privateMsg.getText();
            gottenmsg2 = gottenmsg2.replace(" ", "%20");
            String result = getApi("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + gottenmsg2, "content");
            result = result.replace("{br}", "\n");
            result = result.replace("菲菲", privateMsg.getBotInfo().getBotName());
            sender.sendPrivateMsg(listenedinfo, result);
            Log_settler.writelog("OnPrivate" + String.valueOf(privateMsg.getBotInfo()));
            Log_settler.writelog("bot:" + result);
            Log_settler.writelog(String.valueOf(listenedinfo) + "\n\n\n");
            Thread.sleep(3000);
        }
    }
    @Listen(FriendAddRequest.class)

    public void onRequest(FriendAddRequest friendAddRequest, Setter setter, Sender sender) throws IOException {
        AccountInfo accountInfo = friendAddRequest.getAccountInfo();

        String text = friendAddRequest.getText();
        if (text != null) {
            REQUEST_TEXT_MAP.put(accountInfo.getAccountCode(), text);
        }

        Log_settler.writelog(accountInfo.getAccountNickname()+"("+accountInfo.getAccountCode()+")"+"申请加好友,申请备注："+text+"\n\n\n");
        setter.acceptFriendAddRequest(friendAddRequest.getFlag());

    }
    @Listen(PrivateMsg.class)
    @Filter(value = "二次元",matchType= MatchType.EQUALS)
    public void pic(PrivateMsg privateMsg, Sender sender) throws IOException {
        if (read(".\\cache\\properties\\"+privateMsg.getBotInfo().getBotCode()+".properties","Pic").equals("true")) {
            AccountInfo info = privateMsg.getAccountInfo();
            final CatCodeUtil catUtil = CatCodeUtil.INSTANCE;
            String img = catUtil.toCat("image", true, "url=https://api.ococn.cn/api/img");
            sender.sendPrivateMsg(info, img);
            Log_settler.writelog("OnPrivate");
            Log_settler.writelog("bot"+img+"\n\n\n\n");
        }
    }
    
}
