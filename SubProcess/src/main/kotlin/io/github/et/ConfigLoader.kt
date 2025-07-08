package io.github.et

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.nio.charset.StandardCharsets

object ConfigLoader {
    @JvmField
    var servers: ArrayList<MCServer> = ArrayList()

    @JvmStatic
    @Throws(IOException::class)
    fun load() {
        val configFile = File("./config.txt")
        if (configFile.exists()) {
            val bf = BufferedReader(FileReader(configFile, StandardCharsets.UTF_8))
            for (i in bf.lines().toList()) {
                val a = i.split("\\|\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                servers.add(MCServer(a[0], a[1], a[2], a[3].toLong()))
            }
        }
    }
}
