package love.simbot.example.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import catcode.Neko;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.Listens;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.MessageContent;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.containers.GroupAccountInfo;
import love.forte.simbot.api.message.containers.GroupInfo;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.constant.PriorityConstant;
import love.simbot.example.Log_settler;
import net.sf.json.JSONObject;

@Beans
public class MyPrivateListen {
    @OnPrivate
    public void onPrivateMsg(PrivateMsg privateMsg) throws IOException {
        System.out.println(privateMsg.getText());
        Log_settler.writelog(privateMsg.getText());
        System.out.println(privateMsg.getMsg());
        Log_settler.writelog(privateMsg.getMsg());
        MessageContent msgContent = privateMsg.getMsgContent();
        System.out.println(msgContent);
        Log_settler.writelog(String.valueOf(msgContent));
        List<Neko> imageCats = msgContent.getCats("image");
        System.out.println("img counts: " + imageCats.size());
        for (Neko image : imageCats) {
            System.out.println("Img url: " + image.get("url"));
            Log_settler.writelog("Img url: " + image.get("url"));
        }
        AccountInfo accountInfo = (AccountInfo) privateMsg.getAccountInfo();
        Date date=new Date();
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        String date1= formatter2.format(date);
        Log_settler.writelog(date1);
        System.out.println(accountInfo.getAccountCode());
        Log_settler.writelog("OnPrivate\n"+accountInfo.getAccountCode());
        System.out.println(accountInfo.getAccountNickname());
        Log_settler.writelog(accountInfo.getAccountNickname());
        AccountInfo groupInfo = privateMsg.getAccountInfo();
        Log_settler.writelog(String.valueOf(privateMsg.getAccountInfo()));
        Log_settler.writelog("\n\n");
    }
    @Listen(PrivateMsgRecall.class)
    public void message(PrivateMsgRecall privateMsgRecall, Sender sender) throws IOException {
        sender.sendPrivateMsg(privateMsgRecall, "快说,撤回了什么?!");
        Date date=new Date();
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        String date1= formatter2.format(date);
        Log_settler.writelog(date1);
        Log_settler.writelog("OnPrivate"+String.valueOf(privateMsgRecall.getBotInfo()));
        Log_settler.writelog("bot:"+"快说,撤回了什么?!");
        Log_settler.writelog(String.valueOf(privateMsgRecall.getAccountInfo())+"\n\n\n");
    }
    @Listen(PrivateMsg.class)
    public void Privatemsglisten(PrivateMsg privateMsg, MsgGet msgGet, Sender sender) throws IOException, InterruptedException{
        AccountInfo listenedinfo=privateMsg.getAccountInfo();
        String gottenmsg2=privateMsg.getText();
        URL url=new URL ("http://api.qingyunke.com/api.php?key=free&appid=0&msg="+gottenmsg2);
        InputStream is =url.openStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String result =br.readLine();
        JSONObject obj=JSONObject.fromObject(result);
        result=obj.getString("content");
        result=result.replace("{br}","\n");
        sender.sendPrivateMsg(listenedinfo, result);
        Date date=new Date();
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
        String date1= formatter2.format(date);
        Log_settler.writelog(date1);
        Log_settler.writelog("OnPrivate"+String.valueOf(privateMsg.getBotInfo()));
        Log_settler.writelog("bot:"+result);
        Log_settler.writelog(String.valueOf(listenedinfo)+"\n\n\n");
        Thread.sleep(3000);
    }
    
}
