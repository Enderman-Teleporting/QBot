package com.bot.qbot.Accepter

import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.FriendAddRequestEvent
import org.springframework.stereotype.Component

@Component
class private {
    @Listener
    suspend fun FriendAddRequestEvent.Accept(){

    }
}