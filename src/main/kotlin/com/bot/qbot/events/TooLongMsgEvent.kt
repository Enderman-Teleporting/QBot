package com.bot.qbot.events

import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.GroupMessageEvent
import love.forte.simbot.message.buildMessages
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Beans
class TooLongMsgEvent {
    @Listener
    suspend fun GroupMessageEvent.tooLong(){
        if(messageContent.plainText.length>300){
            author().mute(5, TimeUnit.MINUTES)
            val message=buildMessages{
                at(author().id)
                text("发言过长,下次注意")
            }
            group.send(message)
        }
    }
}