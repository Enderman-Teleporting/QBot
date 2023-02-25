package com.bot.qbot.requests

import com.tools.properties_settler.read
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simboot.filter.MatchType
import love.forte.simbot.event.GroupMessageEvent
import org.springframework.stereotype.Component

@Beans
class GroupRequests {
    @Listener
    @Filter(value = "群名称 ",matchType=MatchType.TEXT_STARTS_WITH)
    suspend fun GroupMessageEvent.groupName() {
        if (read("./cache/properties/" + bot.id + ".properties", "allowGroupNameChanging").equals("true")) {
            val msg = messageContent.plainText.replace("群名称 ", "")
            TODO("The framework has not yet provided such function")
        }
    }
    @Listener
    @Filter(value="头衔 ", matchType = MatchType.TEXT_STARTS_WITH)
    suspend fun GroupMessageEvent.title(){
        if (read("./cache/properties/"+bot.id.toString()+".properties","allowChangeTitle").equals("true")) {
            val msg = messageContent.plainText.replace("头衔 ", "")
            TODO("The framework has not yet provided such function")
        }
    }
}