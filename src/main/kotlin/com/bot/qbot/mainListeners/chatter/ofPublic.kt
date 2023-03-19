package com.bot.qbot.mainListeners.chatter;

import tools.API.getApi
import tools.Log_settler
import tools.properties_settler
import tools.serverSearching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simboot.filter.MatchType
import love.forte.simbot.ID
import love.forte.simbot.event.GroupMessageEvent
import love.forte.simbot.message.Image.Key.toImage
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.buildMessages
import love.forte.simbot.resources.Resource.Companion.toResource
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*
import kotlin.math.ceil


@Component
class ofPublic{
    private val plusOne_:HashMap<String,MessageContent> =HashMap<String,MessageContent>()
    @Listener
    suspend fun GroupMessageEvent.beCalled(){
        if(messageContent.plainText==bot.username){
            group().send("是谁在叫"+bot.username+"呀")
            Log_settler.writelog("Handled one Group Msg Event, Listener func: beCalled")
        }
    }

    @Listener
    @Filter(targets=Filter.Targets(atBot=true), matchType = MatchType.REGEX_CONTAINS)
    suspend fun GroupMessageEvent.chat() {
        var originalMsg = messageContent.plainText
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
            val message = buildMessages {
                text(prev)
                emoji(num.toInt().ID)
                text(aft)
                at(author().id)
            }
            group().send(message)
        } else {
            group().send(result)
        }
        withContext(Dispatchers.IO) {
            Thread.sleep(3000)
        }
        Log_settler.writelog("Handled one Group Msg Event, Listener func: chat")
    }
    @Listener
    suspend fun GroupMessageEvent.plusOne(){
        if (plusOne_.isEmpty()){
            plusOne_["a"] = messageContent
        }
        else{
            plusOne_["b"] = messageContent
        }
        if (plusOne_.size==2 && plusOne_["a"] == plusOne_["b"]){
            group().send(plusOne_["b"]!!)
            plusOne_.clear();
            Log_settler.writelog("Handled one Group Msg Event, Listener func: plusOne")
        }
        else{
            if(plusOne_.size==2) {
                plusOne_.clear()
                plusOne_["a"] = messageContent
            }
        }

    }

    @Listener
    @Filter(value ="二次元", matchType = MatchType.TEXT_EQUALS)
    suspend fun GroupMessageEvent.to(){
        if (properties_settler.read("./cache/properties/${bot.id}.properties", "Pic").equals("true")) {
            val img= URL("https://www.dmoe.cc/random.php").toResource().toImage()
            group().send(img)
        }
    }
    @Listener
    @Filter(value ="MC好图", matchType = MatchType.TEXT_EQUALS)
    suspend fun GroupMessageEvent.MC(){
        if (properties_settler.read("./cache/properties/${bot.id}.properties", "MCPic") =="true"){
            val img= URL("https://enderman-teleporting.github.io/RandomMinecraftPics/api/img/${ceil(Math.random()*100).toInt()}.jpeg").toResource().toImage()
            group().send(img)
        }
    }

    @Listener
    @Filter(value = "查服 ", matchType = MatchType.TEXT_STARTS_WITH )
    suspend fun GroupMessageEvent.MCServerStat() {
        if (properties_settler.read("./cache/properties/${bot.id}.properties", "MCServerStat").equals("true")) {
            val content=messageContent.plainText.replace("查服 ", "")
            val contents: Array<String>
            if (content.contains(":")) {
                try {
                    contents = content.split(":").toTypedArray()
                    val a= serverSearching.search(contents[0], contents[1].toInt())
                    if (serverSearching.searchImg(contents[0], contents[1].toInt()) == null) {
                        group().send(a!!)
                    } else {
                        val messageContent = buildMessages {
                            text(a!!)
                            image(serverSearching.searchImg(contents[0], contents[1].toInt())!!.toResource(UUID.randomUUID().toString()))
                        }
                        group().send(messageContent)
                    }
                } catch (e: Exception) {
                    group().send("yee~,你是不是输错了")
                    Log_settler.writelog("OnPrivate")
                    Log_settler.writelog("bot" + "yee~,你是不是输错了")
                }
            } else if (content.contains("：")) {
                group().send("冒号是英文冒号哦")
            } else {
                val a= serverSearching.search(content)
                if (serverSearching.searchImg(content) == null) {
                    group().send(a!!)
                } else {
                    val MsgContent = buildMessages {
                        text(a!!)
                        image(serverSearching.searchImg(content)!!.toResource(UUID.randomUUID().toString()))
                    }
                    group().send(MsgContent)
                }
            }
            Log_settler.writelog("Handled one Group Msg Event, Listener func: MCServerStat")
        }

    }
}