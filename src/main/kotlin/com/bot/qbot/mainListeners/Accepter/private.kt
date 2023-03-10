package com.bot.qbot.mainListeners.Accepter

import com.tools.Log_settler
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import love.forte.simbot.ExperimentalSimbotApi
import love.forte.simbot.event.FriendAddRequestEvent

@Beans
class private {
    @OptIn(ExperimentalSimbotApi::class)
    @Listener
    suspend fun FriendAddRequestEvent.acceptRequest(){
        accept()
        val id=id.toString()
        Log_settler.writelog("Successfully accepted friend add request from $id")
    }
}