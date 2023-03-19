package com.bot.qbot.mainListeners.Accepter

import love.forte.di.annotation.Beans
import tools.Log_settler
import love.forte.simboot.annotation.Listener
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.event.GroupJoinRequestEvent
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class onGroup {
    @OptIn(ExperimentalSimbotApi::class)
    @Listener
    suspend fun GroupJoinRequestEvent.Accept(){
        accept()
        val bot=bot
        val groupInfo=group()
        val group=bot.group(groupInfo.id)
        val name=requester().username
        val id=id.toString()
        val msg=message
        group!!.send("$name($id)申请加入群聊。\n加群信息:$msg")
        Log_settler.writelog("Sucessfully accepted the group join request from $id")
    }
}