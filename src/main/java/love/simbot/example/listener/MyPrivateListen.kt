package love.simbot.example.listener

import love.forte.common.ioc.annotation.Beans
import love.forte.simbot.annotation.Listen
import love.forte.simbot.annotation.OnPrivate
import love.forte.simbot.api.message.events.MsgGet
import love.forte.simbot.api.message.events.PrivateMsg
import love.forte.simbot.api.message.events.PrivateMsgRecall
import love.forte.simbot.api.sender.Sender
import love.simbot.example.Log_settler.Companion.writelog
import net.sf.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

@Beans
class MyPrivateListen {
    @OnPrivate
    @Throws(IOException::class)
    fun onPrivateMsg(privateMsg: PrivateMsg) {
        writelog(privateMsg.text)
        writelog(privateMsg.msg)
        val msgContent = privateMsg.msgContent
        writelog(msgContent.toString())
        val imageCats = msgContent.getCats("image")
        writelog("img counts: " + imageCats.size)
        for (image in imageCats) {
            writelog("Img url: " + image["url"])
        }
        val accountInfo = privateMsg.accountInfo
        writelog("""
    OnPrivate
    ${accountInfo.accountCode}
    """.trimIndent())
        writelog(accountInfo.accountNickname)
        writelog(privateMsg.accountInfo.toString())
        writelog("\n\n")
    }

    @Listen(PrivateMsgRecall::class)
    @Throws(IOException::class)
    fun message(privateMsgRecall: PrivateMsgRecall, sender: Sender) {
        sender.sendPrivateMsg(privateMsgRecall, "快说,撤回了什么?!")
        writelog("OnPrivate" + privateMsgRecall.botInfo.toString())
        writelog("bot:" + "快说,撤回了什么?!")
        writelog("""
    ${privateMsgRecall.accountInfo}
    
    
    
    """.trimIndent())
    }

    @Listen(PrivateMsg::class)
    @Throws(IOException::class, InterruptedException::class)
    fun Privatemsglisten(privateMsg: PrivateMsg, msgGet: MsgGet?, sender: Sender) {
        val listenedinfo = privateMsg.accountInfo
        val gottenmsg2 = privateMsg.text
        val url = URL("http://api.qingyunke.com/api.php?key=free&appid=0&msg=$gottenmsg2")
        val `is` = url.openStream()
        val br = BufferedReader(InputStreamReader(`is`))
        var result = br.readLine()
        val obj = JSONObject.fromObject(result)
        result = obj.getString("content")
        result = result.replace("{br}", "\n")
        sender.sendPrivateMsg(listenedinfo, result)
        writelog("OnPrivate" + privateMsg.botInfo.toString())
        writelog("bot:$result")
        writelog("""
    $listenedinfo
    
    
    
    """.trimIndent())
        Thread.sleep(3000)
    }
}