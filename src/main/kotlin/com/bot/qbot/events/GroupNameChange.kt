package com.bot.qbot.events

import com.tools.Log_settler
import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import net.mamoe.mirai.event.events.GroupNameChangeEvent

@Beans
class GroupNameChange {
    @Listener
    suspend fun GroupNameChangeEvent.nameChanged(){Log_settler.writelog("The group name of ${group.id} was changed from $origin into $new")}
}