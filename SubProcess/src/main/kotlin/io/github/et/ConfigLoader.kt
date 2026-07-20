package io.github.et

import java.io.File
import java.io.FileReader
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.*

object ConfigLoader {
    @JvmField
    var servers: ArrayList<MCServer> = ArrayList()

    @JvmStatic
    @Throws(IOException::class)
    fun load() {
        val root=File("./mcservers")
        if(!root.exists()){
            return
        }
        for (i in root.listFiles()!!) {
            if(i.name.lowercase(Locale.getDefault()).endsWith(".properties")){
                val a =Properties()
                a.load(FileReader(i, StandardCharsets.UTF_8))
                servers.add(MCServer(a["name"].toString(), a["working_dir"].toString(),a["command"].toString(),a["group"].toString().toLong(),a["useBackup"].toString().toBoolean(),a["death_msg"].toString().toBoolean() ,a["useAdvancement"].toString().toBoolean()))
            }
        }

    }
}
