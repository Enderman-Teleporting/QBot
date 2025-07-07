package io.github.et

import io.github.et.ConfigLoader.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object SubMain {
    private val processMap: MutableMap<MCServer, Process> = ConcurrentHashMap()
    private var bot: Process? = null
    lateinit var OS:BufferedWriter

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val port = args[0].toInt()
            val s = Socket("127.0.0.1", port)
            val `is` = BufferedReader(InputStreamReader(s.getInputStream()))
            OS = BufferedWriter(OutputStreamWriter(s.getOutputStream()))
            load()
            val root = File(".")
            var QBotRunPathName: String? = null
            for (i in Objects.requireNonNull<Array<File>>(root.listFiles())) {
                if (i.name.contains("NapCat")&&i.name.contains("Shell")&&i.isDirectory) {
                    QBotRunPathName = i.absolutePath
                }
            }
            if (QBotRunPathName == null) {
                throw RuntimeException("QBotRunPathName is null")
            }
            val builder = ProcessBuilder("$QBotRunPathName\\napcat.bat").directory(File(QBotRunPathName))
            bot = builder.start()
            for (server in ConfigLoader.servers) {
                val pb = ProcessBuilder(*server.command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()).directory(
                    File(server.workingDir)
                )
                val process = pb.start()
                processMap[server] = process
            }
            Thread {
                try {
                    while (true) {
                        val a = `is`.readLine()
                        if(a == null) {
                            continue
                        }
                        if (a.startsWith("[") && a.contains("]/")) {
                            val name = getContent(a)
                            val cmd = a.substring(name.length + 3)
                            for (server in ConfigLoader.servers) {
                                if (server.name == name) {
                                    processMap[server]!!.outputStream.write((cmd + "\r\n").toByteArray(StandardCharsets.UTF_8))
                                    processMap[server]!!.outputStream.flush()
                                }
                            }
                        }else if(a.contains("<")&&a.contains(">")){
                            val name= getContent(a)
                            for(server in ConfigLoader.servers){
                                if(server.name==name){
                                    processMap[server]!!.outputStream.write(("msg @a ${getTalkCont(a)}\r\n").toByteArray(StandardCharsets.UTF_8))
                                    processMap[server]!!.outputStream.flush()
                                }
                            }
                        }
                    }
                } catch (e: IOException) {
                    deal()
                }
            }.start()
            for(i in ConfigLoader.servers){
                Thread {
                    var os = BufferedWriter(OutputStreamWriter(processMap[i]?.outputStream))
                    var iss = BufferedReader(InputStreamReader(processMap[i]?.inputStream))
                    while(true){
                        var a = iss.readLine()
                        if(a == null) break
                        OS.write("[${i.name}]$a\r\n")
                        OS.flush()
                    }
                }.start()
            }
            Thread{
                var os=BufferedWriter(OutputStreamWriter(bot?.outputStream))
                var iss=BufferedReader(InputStreamReader(bot?.inputStream))
                while(true){
                    var a = iss.readLine()
                    if(a == null) {
                        continue
                    }
                    OS.write("[]$a\r\n")
                    OS.flush()
                }
            }.start()
        } catch (e: Exception) {
            deal()
        }
    }

    private fun getContent(str: String): String {
        Objects.requireNonNull(str)
        val start = str.indexOf('[')
        if (start == -1) {
            return ""
        }
        val end = str.indexOf(']', start + 1)
        if (end == -1) {
            return ""
        }
        return str.substring(start + 1, end)
    }

    private fun getTalkCont(s: String): String {
        val start = s.indexOf('<')
        if (start == -1) {
            return ""
        }
        var count = 1
        for (i in start + 1 until s.length) {
            val c = s[i]
            if (c == '<') {
                count++
            } else if (c == '>') {
                count--
            }
            if (count == 0) {
                return s.substring(start)
            }
        }
        return ""
    }
    fun deal(){
        if (!processMap.isEmpty()) {
            for (server in ConfigLoader.servers) {
                val process= processMap[server]!!
                if(process.isAlive) {
                    GlobalScope.launch {
                        process.outputStream.write("stop\r\n".toByteArray(StandardCharsets.UTF_8))
                        process.outputStream.flush()
                        Thread.sleep(10000)
                        if(process.isAlive) {
                            process.destroyForcibly()
                        }
                    }
                }
            }
        }
        if(bot!= null) {
            bot!!.destroyForcibly()
        }
    }
    //TODO 1.加载类问题
    //TODO 2.进程没关掉
}
