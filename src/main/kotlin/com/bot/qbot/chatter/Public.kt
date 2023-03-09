package com.bot.qbot.chatter;
import com.tools.API.getApi
import com.tools.Log_settler
import com.tools.properties_settler.read
import com.tools.serverSearching.*
import com.bot.qbot.Accepter.group
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simboot.filter.MatchType
import love.forte.simbot.ID
import love.forte.simbot.event.GroupMessageEvent
import love.forte.simbot.message.Image.Key.toImage
import love.forte.simbot.message.buildMessages
import love.forte.simbot.resources.Resource.Companion.toResource
import java.net.URL
import java.util.*
import kotlin.math.ceil

@Beans
class Public{
    @Listener
    suspend fun GroupMessageEvent.beCalled(){
        if(messageContent.plainText==bot.username){
            group().send("是谁在叫"+bot.username+"呀")
        }
    }

    @Listener
    @Filter(targets=Filter.Targets(atBot=true), matchType = MatchType.REGEX_CONTAINS)
    suspend fun GroupMessageEvent.chat(){
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
                    face(num.toInt().ID)
                    text(aft)
                    at(author().id)
                }
                group().send(message)
            } else {
                group.send(result)
            }
            Thread.sleep(3000)
        }
    }
    
}