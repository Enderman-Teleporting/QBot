package com.bot.qbot.mainListeners.requests

import com.tools.Log_settler
import com.tools.properties_settler.read
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simboot.filter.MatchType
import love.forte.simbot.component.mirai.event.MiraiGroupMessageEvent
import love.forte.simbot.event.GroupMessageEvent

@Beans
class GroupRequests {
    @Listener
    @Filter(value = "群名称 ",matchType=MatchType.TEXT_STARTS_WITH)
    suspend fun GroupMessageEvent.groupName() {
        if (read("./cache/properties/" + bot.id + ".properties", "allowGroupNameChanging").equals("true")) {
            val msg = messageContent.plainText.replace("群名称 ", "")
            TODO("The framework hasn't supported such function")

        }
    }
    @Listener
    @Filter(value="头衔 ", matchType = MatchType.TEXT_STARTS_WITH)
    suspend fun MiraiGroupMessageEvent.title(){
        if (read("./cache/properties/"+bot.id+".properties","allowChangeTitle").equals("true")) {
            val msg = messageContent.plainText.replace("头衔 ", "")
            author().specialTitle=msg
            send("已给予头衔")
            Log_settler.writelog("Request processed: Title Request in group ${group().id} from ${author().id}")
        }
    }
    @Listener
    @Filter(value="我要管理",matchType=MatchType.TEXT_EQUALS)
    suspend fun MiraiGroupMessageEvent.admin(){
        if (read("./cache/properties/"+bot.id+".properties","allowGroupNameChanging").equals("true")) {
            author().modifyAdmin(true)
            send("已给予管理")
            Log_settler.writelog("Request processed: Admin Request in group ${group().id} from ${author().id}")
            Thread.sleep(60000)
            author().modifyAdmin(false)
            send("时间已到，收回管理权限")
            Log_settler.writelog("Processed one admin request")
        }
    }
}