package love.simbot.example

import love.forte.simbot.annotation.SimbotApplication
import love.forte.simbot.annotation.SimbotResource
import love.forte.simbot.core.SimbotApp
import love.simbot.example.Log_settler.Companion.settle
import java.io.IOException

@SimbotApplication(SimbotResource(value = "simbot.yml", orIgnore = true), SimbotResource(value = "simbot-dev.yml", orIgnore = true, command = "dev")) // @SimbotApplication

object SimbotExampleApplication {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        settle()
        SimbotApp.run(SimbotExampleApplication::class.java, *args)
    }
}