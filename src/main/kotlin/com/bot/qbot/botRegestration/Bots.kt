package com.bot.qbot.botRegestration

import love.forte.di.annotation.Beans
import love.forte.simboot.annotation.Listener
import love.forte.simbot.event.GroupMessageEvent
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

@Beans
class Bots{
    @Listener
    fun GroupMessageEvent.settle() {
        File("./cache/properties").mkdirs()
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
