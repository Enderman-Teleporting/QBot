package io.github.et.games.iAmAWolf

import io.github.et.exceptions.GameCrashedException
import io.github.ettoolset.tools.logger.Logger
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.GroupMessageEvent
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
class IAmAWolf : SimpleListenerHost() {
    private val gameMap:MutableMap<Long,Game> =mutableMapOf()
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        val logger: Logger = Logger.getDeclaredLogger()
        logger.error("Game I am a wolf crashed, info as below")
        throw GameCrashedException("Game I am a wolf crashed")
    }
    @EventHandler
    suspend fun GroupMessageEvent.iAmAWolf():ListeningStatus{

    }
    
}