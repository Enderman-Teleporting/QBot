package com.bot.qbot.requests

import com.tools.properties_settler.read
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.GroupMessageEvent
import org.springframework.stereotype.Component

@Component
class GroupRequests {
    @Listener
    @Filter(value = "群名称 ")
    suspend fun GroupMessageEvent.groupName() {
        if (read("./cache/properties/" + bot.id + ".properties", "allowGroupNameChanging").equals("true")) {
            val msg = messageContent.plainText.replace("群名称 ", "")
            TODO()
        }
    }
}