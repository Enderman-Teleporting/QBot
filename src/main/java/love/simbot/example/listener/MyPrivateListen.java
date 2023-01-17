package love.simbot.example.listener;

import catcode.CatCodeUtil;
import catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.MessageContentBuilder;
import love.forte.simbot.api.message.MessageContentBuilderFactory;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.FriendAddRequest;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
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
import static love.simbot.example.tools.serverSearching.search;

@Beans
public class MyPrivateListen {
    private static final Map<String, String> REQUEST_TEXT_MAP = new ConcurrentHashMap<>();
    @Depend
    private MessageContentBuilderFactory messageBuilderFactory;
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
    }
    @Listen(PrivateMsgRecall.class)
    public void message(PrivateMsgRecall privateMsgRecall, Sender sender) throws IOException {
        sender.sendPrivateMsg(privateMsgRecall, "快说,撤回了什么?!");
        Log_settler.writelog("OnPrivate"+String.valueOf(privateMsgRecall.getBotInfo()));
        Log_settler.writelog("bot:"+"快说,撤回了什么?!");
        Log_settler.writelog(String.valueOf(privateMsgRecall.getAccountInfo()));
    }
    @Listen(PrivateMsg.class)
    public void Privatemsglisten(PrivateMsg privateMsg, Sender sender) throws IOException, InterruptedException{
        if(!(privateMsg.getText().equals("二次元")||privateMsg.getText().equals("MC好图")||privateMsg.getText().startsWith("查服 "))) {
            AccountInfo listenedinfo = privateMsg.getAccountInfo();
            String gottenmsg2 = privateMsg.getText();
            gottenmsg2 = gottenmsg2.replace(" ", "%20");
            String result = getApi("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + gottenmsg2, "content");
            result = result.replace("菲菲", privateMsg.getBotInfo().getBotName());
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
                        .at(listenedinfo)
                        .build();
                sender.sendPrivateMsg(listenedinfo, message);
                Log_settler.writelog("OnPrivate" + String.valueOf(privateMsg.getBotInfo()));
                Log_settler.writelog("bot:" + message);
                Log_settler.writelog(String.valueOf(listenedinfo));
            }
            else{
                MessageContentBuilder builder=messageBuilderFactory.getMessageContentBuilder();
                MessageContent content=builder
                        .text(result)
                        .at(listenedinfo)
                        .build();
                sender.sendPrivateMsg(listenedinfo,result);
                Log_settler.writelog("OnPrivate" + String.valueOf(privateMsg.getBotInfo()));
                Log_settler.writelog("bot:" + result);
                Log_settler.writelog(String.valueOf(listenedinfo));
            }
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
        if (read("./cache/properties/"+privateMsg.getBotInfo().getBotCode()+".properties","Pic").equals("true")) {
            AccountInfo info = privateMsg.getAccountInfo();
            final CatCodeUtil catUtil = CatCodeUtil.INSTANCE;
            String img = catUtil.toCat("image", true, "url=https://www.dmoe.cc/random.php");
            sender.sendPrivateMsg(info, img);
            Log_settler.writelog("OnPrivate");
            Log_settler.writelog("bot"+img);
        }
    }
    @Listen(PrivateMsg.class)
    @Filter(value = "MC好图",matchType= MatchType.EQUALS)
    public void MC(PrivateMsg privateMsg, Sender sender) throws IOException {
        if (read("./cache/properties/"+privateMsg.getBotInfo().getBotCode()+".properties","MCPic").equals("true")) {
            final AccountInfo info = privateMsg.getAccountInfo();
            final CatCodeUtil catUtil = CatCodeUtil.INSTANCE;
            String img = catUtil.toCat("image", true, "url=https://api.lantianyun.tk");
            sender.sendPrivateMsg(info, img);
            Log_settler.writelog("OnPrivate");
            Log_settler.writelog("bot"+img);
        }
    }
    @OnPrivate
    @Filter(value = "查服 ",matchType = MatchType.STARTS_WITH)
    public void MCServerStat (PrivateMsg privateMsg,Sender sender) throws IOException {
        if(read("./cache/properties/"+privateMsg.getBotInfo().getBotCode()+".properties","MCServerStat").equals("true")) {
            final var info = privateMsg.getAccountInfo();
            var content = privateMsg.getText().replace("查服 ", "");
            String[] contents;
            if (content.contains(":")) {
                try {
                    contents = content.split(":");
                    sender.sendPrivateMsg(info, search(contents[0], Integer.parseInt(contents[1])));
                    Log_settler.writelog("OnPrivate");
                    Log_settler.writelog("bot" + search(contents[0], Integer.parseInt(contents[1])));
                }
                catch (Exception e) {
                    sender.sendPrivateMsg(info, "yee~,你是不是输错了");
                    Log_settler.writelog("OnPrivate");
                    Log_settler.writelog("bot" + "yee~,你是不是输错了");
                }
            }
            else if (content.contains("：")) {
                sender.sendPrivateMsg(info, "冒号是英文冒号哦");
                Log_settler.writelog("OnPrivate");
                Log_settler.writelog("bot" + "冒号是英文冒号哦");
            }
            else {
                sender.sendPrivateMsg(info, search(content));
                Log_settler.writelog("OnPrivate");
                Log_settler.writelog("bot" + search(content));
            }
        }
    }
}
