package com.bot.qbot.events

import com.tools.Log_settler
import love.forte.simboot.annotation.Listener
import net.mamoe.mirai.event.events.MemberPermissionChangeEvent
import org.springframework.stereotype.Component

@Component
class PermissionChange {
    @Listener
    suspend fun MemberPermissionChangeEvent.permit(){
        if(origin.level>new.level){ Log_settler.writelog("A permission decrease was seen in ${group.id}") }
        else{Log_settler.writelog("A permission increase was seen in ${group.id}")}
    }
}