package love.simbot.example.listener

import love.forte.common.ioc.annotation.Beans
import love.forte.common.ioc.annotation.Depend
import love.forte.simbot.annotation.Filter
import love.forte.simbot.annotation.Listen
import love.forte.simbot.annotation.OnGroup
import love.forte.simbot.api.message.MessageContentBuilderFactory
import love.forte.simbot.api.message.containers.AccountInfo
import love.forte.simbot.api.message.events.GroupMsg
import love.forte.simbot.api.message.events.GroupMsgRecall
import love.forte.simbot.api.message.events.MsgGet
import love.forte.simbot.api.sender.Sender
import love.forte.simbot.filter.MatchType
import love.simbot.example.Log_settler.Companion.writelog
import net.sf.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL

@Beans
class MyGroupListen {
    var renyunyiren = HashMap<String, String>()

    @Depend
    private val messageBuilderFactory: MessageContentBuilderFactory? = null
    @OnGroup
    @Throws(IOException::class)
    fun onGroupMsg(groupMsg: GroupMsg) {
        writelog(groupMsg.text)
        writelog(groupMsg.msg)
        val msgContent = groupMsg.msgContent
        writelog(msgContent.toString())
        val imageCats = msgContent.getCats("image")
        writelog("img counts: " + imageCats.size)
        for (image in imageCats) {
            writelog("Img url: " + image["url"])
        }
        val accountInfo = groupMsg.accountInfo
        writelog("""
    OnGroup
    ${accountInfo.accountCode}
    """.trimIndent())
        writelog(accountInfo.accountNickname)
        val groupInfo = groupMsg.groupInfo
        writelog(groupMsg.groupInfo.toString())
        writelog(groupInfo.groupCode)
        writelog(groupInfo.groupName)
        writelog("\n\n")
    }

    @Listen(GroupMsg::class)
    @Filter(value = "菲菲", matchType = MatchType.EQUALS)
    @Throws(IOException::class)
    fun beCalled(groupmsg: GroupMsg, sender: Sender) {
        val groupInfo1 = groupmsg.groupInfo
        sender.sendGroupMsg(groupInfo1, "是谁在叫菲菲啊")
        writelog("OnGroup" + groupmsg.botInfo.toString())
        writelog(groupInfo1.toString())
        writelog("bot:是谁在叫菲菲呀\n\n\n")
    }

    @Listen(GroupMsg::class)
    @Filter(atBot = true, matchType = MatchType.CONTAINS)
    @Throws(IOException::class, InterruptedException::class)
    fun Groupmsglisten(groupMsg: GroupMsg, msgGet: MsgGet?, sender: Sender) {
        val listenedgroupinfo = groupMsg.groupInfo
        val accountInfoabc: AccountInfo = groupMsg.accountInfo
        var gottenmsg1 = groupMsg.text
        gottenmsg1 = gottenmsg1.replace(" ", "%20")
        val url = URL("http://api.qingyunke.com/api.php?key=free&appid=0&msg=$gottenmsg1")
        val `is` = url.openStream()
        val br = BufferedReader(InputStreamReader(`is`))
        var result = br.readLine()
        val obj = JSONObject.fromObject(result)
        result = obj.getString("content")
        result = result.replace("{br}", "\n")
        val builder2 = messageBuilderFactory!!.getMessageContentBuilder()
        val message12 = builder2
                .text(result)
                .at(accountInfoabc)
                .build()
        sender.sendGroupMsg(listenedgroupinfo, message12)
        writelog("OnGroup" + groupMsg.botInfo.toString())
        writelog("bot:$result")
        writelog("""
    $listenedgroupinfo
    
    
    
    """.trimIndent())
        Thread.sleep(3000)
    }

    @Listen(GroupMsgRecall::class)
    @Throws(IOException::class)
    fun message(groupMsgRecall: GroupMsgRecall, sender: Sender) {
        val groupinforec = groupMsgRecall.groupInfo
        sender.sendGroupMsg(groupinforec, "快说,撤回了什么?!")
        writelog("OnGroup" + groupMsgRecall.botInfo.toString())
        writelog("bot:" + "快说,撤回了什么?!")
        writelog("""
    $groupinforec
    
    
    
    """.trimIndent())
    }

    @Listen(GroupMsg::class)
    fun renyunyiyun(groupMsg: GroupMsg, sender: Sender) {
        if (renyunyiren.isEmpty()) {
            renyunyiren["a"] = groupMsg.msg
        } else {
            renyunyiren["b"] = groupMsg.msg
        }
        if (renyunyiren.size == 2 && renyunyiren["a"] == renyunyiren["b"]) {
            sender.sendGroupMsg(groupMsg.groupInfo, renyunyiren["b"]!!)
            renyunyiren.clear()
        } else {
            if (renyunyiren.size == 2) {
                renyunyiren.clear()
                renyunyiren["a"] = groupMsg.msg
            }
        }
    }
}