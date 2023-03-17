package com.bot.qbot

import tools.Log_settler
import tools.properties_settler
import love.forte.simboot.core.SimbootApp
import love.forte.simboot.core.SimbootApplication
import love.forte.simboot.spring.autoconfigure.EnableSimbot
import love.forte.simbot.application.bots
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableSimbot
@EnableScheduling
class SimbotExampleApplication

suspend fun main(args: Array<String>) {
        Log_settler.settle()
        properties_settler.settle()
        try {
                val a = SimbootApp.run(SimbotExampleApplication::class, *args)
                a.join()
                if(a.isActive){
                        Log_settler.writelog("Bot Registration Successful!")
                }
        }
        catch (e:Exception){
                Log_settler.writelogError("Bot Registration Failed!\nError Info:$e")
        }
}