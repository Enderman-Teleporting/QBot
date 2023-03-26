package com.bot.qbot.mainListeners.events

import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import net.mamoe.mirai.event.events.MessageRecallEvent
import org.springframework.stereotype.Component

@Component
class Recall {
    @Listener
    suspend fun MessageRecallEvent.GroupRecall.groupRecall(){ group.sendMessage("快说,撤回了什么?") }
    @Listener
    suspend fun MessageRecallEvent.FriendRecall.friendRecall(){author.sendMessage("快说,撤回了什么?")}

}