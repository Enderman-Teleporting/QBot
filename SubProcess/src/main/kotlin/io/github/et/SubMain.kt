package io.github.et

import io.github.et.ConfigLoader.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.lang.System
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.system.exitProcess

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
            val `is` = BufferedReader(InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8))
            OS = BufferedWriter(OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))
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
                        }else if(a.contains("<".toRegex())&&a.contains(">".toRegex())&&(!a.contains("\\[Server]".toRegex()))){
                            val name= getContent(a)
                            for(server in ConfigLoader.servers){
                                if(server.name==name){
                                    processMap[server]!!.outputStream.write(("say ${a.substring(a.indexOf("<"))}\r\n").toByteArray(StandardCharsets.UTF_8))
                                    processMap[server]!!.outputStream.flush()
                                }
                            }
                        }else if(a.startsWith("restart ")){
                            val name=a.substring(8)
                            for(server in ConfigLoader.servers){
                                if(server.name==name){
                                    if(processMap[server]!!.isAlive){
                                        processMap[server]!!.destroy()
                                        processMap[server]!!.waitFor()
                                    }
                                    val pb = ProcessBuilder(*server.command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()).directory(
                                        File(server.workingDir)
                                    )
                                    val process = pb.start()
                                    processMap[server] = process
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
                    BufferedWriter(OutputStreamWriter(processMap[i]?.outputStream, StandardCharsets.UTF_8))
                    var iss = BufferedReader(InputStreamReader(processMap[i]?.inputStream, StandardCharsets.UTF_8))
                    while(true){
                        var a = iss.readLine()
                        if(a == null) break
                        OS.write("[${i.name}]$a\r\n")
                        OS.flush()
                    }
                }.start()
            }
            Thread{
                var os=BufferedWriter(OutputStreamWriter(bot?.outputStream, StandardCharsets.UTF_8))
                var iss=BufferedReader(InputStreamReader(bot?.inputStream, StandardCharsets.UTF_8))
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
            bot!!.destroy()
            ProcessBuilder("taskkill","/F","/IM","NapCatWinBootMain.exe").start()
            ProcessBuilder("taskkill","/F","/IM","QQ.exe").start()
            Thread.sleep(10000)
            if (bot!!.isAlive) {
                bot!!.destroyForcibly()
            }
        }
        exitProcess(0)
    }
}