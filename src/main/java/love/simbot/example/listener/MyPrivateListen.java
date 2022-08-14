package love.simbot.example.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.annotation.Listen;
import love.forte.simbot.annotation.Listens;
import love.forte.simbot.api.message.containers.AccountInfo;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.message.events.PrivateMsgRecall;
import love.forte.simbot.api.sender.Sender;
import love.forte.simbot.constant.PriorityConstant;
import net.sf.json.JSONObject;

@Beans
public class MyPrivateListen {
    @Listen(PrivateMsgRecall.class)
    public void message(PrivateMsgRecall privateMsgRecall, Sender sender){
        sender.sendPrivateMsg(privateMsgRecall, "快说,撤回了什么?!");
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
        Thread.sleep(3000);
    }
    
}
