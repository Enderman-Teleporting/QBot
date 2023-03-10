package com.bot.qbot.mainListeners.msgCounts

import com.tools.Log_settler
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Filter
import love.forte.simboot.annotation.Listener
import love.forte.simboot.filter.MatchType
import love.forte.simbot.event.FriendMessageEvent
import love.forte.simbot.event.GroupMessageEvent

@Beans
class MsgCounts {
    @Listener
    @Filter(value="消息统计", matchType = MatchType.TEXT_EQUALS)
    suspend fun GroupMessageEvent.msgCountGroup(){
        group().send("$bot.contactCount()")
        Log_settler.writelog("Handled one Group Msg Event, Listener func: msgCountGroup")
    }
    @Listener
    @Filter(value="消息统计", matchType = MatchType.TEXT_EQUALS)
    suspend fun FriendMessageEvent.msgCountPrivate(){
        friend().send("$bot.contactCount()")
        Log_settler.writelog("Handled one Private Msg Event, Listener func: msgCountPrivate")
    }
}