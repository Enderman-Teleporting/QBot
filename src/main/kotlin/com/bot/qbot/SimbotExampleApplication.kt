package com.bot.qbot

import com.tools.Log_settler
import com.tools.properties_settler
import love.forte.simboot.core.SimbootApp
import love.forte.simboot.core.SimbootApplication

@SimbootApplication
class Main

suspend fun main(args: Array<String>) {
        Log_settler.settle()
        properties_settler.settle()
        SimbootApp.run(Main::class, *args).join()
}