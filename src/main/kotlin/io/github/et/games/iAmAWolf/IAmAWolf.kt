package io.github.et.games.iAmAWolf

import net.mamoe.mirai.event.SimpleListenerHost
import kotlin.coroutines.CoroutineContext

class IAmAWolf : SimpleListenerHost() {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        super.handleException(context, exception)
    }

}