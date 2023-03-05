package com.bot.qbot.events

import com.tools.Log_settler
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.FriendDecreaseEvent
import love.forte.simbot.event.FriendIncreaseEvent
import love.forte.simbot.event.GroupMemberDecreaseEvent
import love.forte.simbot.event.GroupMemberIncreaseEvent

@Beans
class MemberChange {
    @Listener
    suspend fun GroupMemberIncreaseEvent.haveJoined(){ group().send("欢迎新人!我是机器人${bot.username},很高兴见到你") }

    @Listener
    suspend fun GroupMemberDecreaseEvent.haveLeft(){ group().send("${member().username}离开了我们...") }

    @Listener
    suspend fun FriendDecreaseEvent.tellDecrease(){Log_settler.writelog("Your bot's friend ${friend().username}(${friend().id}) left it")}

    @Listener
    suspend fun FriendIncreaseEvent.tellIncrease(){Log_settler.writelog("Your bot's new friend: ${friend().username}(${friend().id})")}
}