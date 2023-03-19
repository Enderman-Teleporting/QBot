package com.bot.qbot.mainListeners.chatter

import love.forte.di.annotation.Beans
import tools.API.getApi
import tools.Log_settler
import tools.properties_settler.read
import tools.serverSearching.search
import tools.serverSearching.searchImg
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simboot.filter.MatchType
import love.forte.simbot.ID
import love.forte.simbot.event.FriendMessageEvent
import love.forte.simbot.message.Image.Key.toImage
import love.forte.simbot.message.buildMessages
import love.forte.simbot.resources.Resource.Companion.toResource
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*
import kotlin.math.ceil


@Component
class ofPrivate {
    @Listener
    suspend fun FriendMessageEvent.beCalled(){
        if(messageContent.plainText == bot.username){
            friend().send("是谁在叫"+bot.username+"呀")
        }
        Log_settler.writelog("Handled one Private Msg Event, Listener func: beCalled")
    }

    @Listener
    suspend fun FriendMessageEvent.privateReply() {
        if (messageContent.plainText != "二次元" || messageContent.plainText!="MC好图"|| !messageContent.plainText.startsWith("查服 ")) {
            var originalMsg= messageContent.plainText
            originalMsg = originalMsg.replace(" ", "%20")
            var result = getApi("http://api.qingyunke.com/api.php?key=free&appid=0&msg=$originalMsg", "content")
            result = result.replace("菲菲", bot.username)
            if (result.contains("{") && result.contains("}")) {
                var prev = ""
                var aft = ""
                var num = ""
                val MsgArray = result.toCharArray()
                var i = 0
                while (MsgArray[i] != '{') {
                    prev += MsgArray[i]
                    i++
                }
                for (j in 1 until MsgArray.size) {
                    if (MsgArray[j - 1] == ':') {
                        var k = j
                        while (MsgArray[k + 1] != '}') {
                            num += MsgArray[k]
                            k++
                        }
                    }
                    if (MsgArray[j - 1] == '}') {
                        for (l in j until MsgArray.size) {
                            aft += MsgArray[l]
                        }
                    }
                }
                val message= buildMessages {
                    text(prev)
                    emoji(num.toInt().ID)
                    text(aft)
                }
                friend().send(message)
            } else {
                friend.send(result)
            }
            Log_settler.writelog("Handled one Private Msg Event, Listener func: privateReply")
            Thread.sleep(3000)
        }
    }
    @Listener
    @Filter(value ="二次元", matchType = MatchType.TEXT_EQUALS)
    suspend fun FriendMessageEvent.to(){
        if (read("./cache/properties/${bot.id}.properties","Pic").equals("true")) {
            val img= URL("https://www.dmoe.cc/random.php").toResource().toImage()
            friend().send(img)
        }
    }
    @Listener
    @Filter(value ="MC好图", matchType = MatchType.TEXT_EQUALS)
    suspend fun FriendMessageEvent.MC(){
        if (read("./cache/properties/${bot.id}.properties","MCPic")=="true"){
            val img=URL("https://enderman-teleporting.github.io/RandomMinecraftPics/api/img/${ceil(Math.random()*100).toInt()}.jpeg").toResource().toImage()
            friend().send(img)
        }
    }

    @Listener
    @Filter(value = "查服 ", matchType = MatchType.TEXT_STARTS_WITH )
    suspend fun FriendMessageEvent.MCServerStat() {
        if (read("./cache/properties/${bot.id}.properties", "MCServerStat").equals("true")) {
            val content=messageContent.plainText.replace("查服 ", "")
            val contents: Array<String>
            if (content.contains(":")) {
                try {
                    contents = content.split(":").toTypedArray()
                    val a= search(contents[0], contents[1].toInt())
                    if (searchImg(contents[0], contents[1].toInt()) == null) {
                        friend().send(a!!)
                    } else {
                        val messageContent = buildMessages {
                            text(a!!)
                            image(searchImg(contents[0], contents[1].toInt())!!.toResource(UUID.randomUUID().toString()))
                        }
                        friend().send(messageContent)
                    }
                } catch (e: Exception) {
                    friend().send("yee~,你是不是输错了")
                    Log_settler.writelog("OnPrivate")
                    Log_settler.writelog("bot" + "yee~,你是不是输错了")
                }
            } else if (content.contains("：")) {
                friend().send("冒号是英文冒号哦")
            } else {
                val a= search(content)
                if (searchImg(content) == null) {
                    friend().send(a!!)
                } else {
                    val MsgContent = buildMessages {
                        text(a!!)
                        image(searchImg(content)!!.toResource(UUID.randomUUID().toString()))
                    }
                    friend().send(MsgContent)
                }
            }
            Log_settler.writelog("Handled one Private Msg Event, Listener func: MCServerStat")
        }
    }
}