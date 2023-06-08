package com.bot.qbot.botRegistration

import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.GroupMessageEvent
import org.springframework.stereotype.Component
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

@Beans
class Bots{
    @Listener
    fun GroupMessageEvent.settle() {
        val fileName = "./cache/properties/${bot.id}.properties"
        try {
            val f = File(fileName)
            if (!f.exists()) {
                f.createNewFile()
                val bw = BufferedWriter(FileWriter(fileName))
                bw.write("Pic=true\n300WordsBan=true\nallowGroupNameChanging=true\nallowChangeTitle=true\nallowGettingAdmin=false\nMCPic=true\nMCServerStat=false\n")
                bw.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
