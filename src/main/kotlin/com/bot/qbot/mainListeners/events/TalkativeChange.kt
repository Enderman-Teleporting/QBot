package com.bot.qbot.mainListeners.events

import com.tools.Log_settler
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import love.forte.simbot.FragileSimbotApi
import love.forte.simbot.ID
import love.forte.simbot.bot.Bot
import love.forte.simbot.bot.OriginBotManager
import love.forte.simbot.message.buildMessages
import net.mamoe.mirai.event.events.GroupTalkativeChangeEvent

@Beans
class TalkativeChange {
    @OptIn(FragileSimbotApi::class)
    @Listener
    suspend fun GroupTalkativeChangeEvent.talkative(){
        Log_settler.writelog("Group ${group.id} talkative was changed into ${now.id}")
        val message= buildMessages {
            at(previous.id.ID)
            text("的龙王被")
            at(now.id.ID)
            text("抢走了")
        }
        val bot: Bot = OriginBotManager.getBot(bot.id.ID)
        bot.getGroup(group.id.ID)?.send(message)
    }
}