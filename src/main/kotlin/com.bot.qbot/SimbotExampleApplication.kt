package com.bot.qbot

import com.bot.qbot.tools.Log_settler
import com.bot.qbot.tools.properties_settler
import love.forte.simboot.core.SimbootApp
import love.forte.simboot.core.SimbootApplication
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.*

@SimbootApplication
class Main

suspend fun main(args: Array<String>) {
        val time = ZonedDateTime.now()
        val date1 = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val logger = Logger.getLogger(date1)
        logger.level = Level.INFO
        Log_settler.settle()
        properties_settler.settle()
        SimbootApp.run(Main::class, *args).join()

}