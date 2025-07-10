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
                                        processMap[server]!!.outputStream.write(("stop\r\n").toByteArray(StandardCharsets.UTF_8))
                                        processMap[server]!!.outputStream.flush()
                                        processMap[server]!!.waitFor()
                                    }
                                    val pb = ProcessBuilder(*server.command.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()).directory(
                                        File(server.workingDir)
                                    )
                                    val process = pb.start()
                                    processMap[server] = process
                                    GlobalScope.launch {
                                        BufferedWriter(OutputStreamWriter(process.outputStream, StandardCharsets.UTF_8))
                                        var iss = BufferedReader(InputStreamReader(process.inputStream, StandardCharsets.UTF_8))
                                        while(true){
                                            var a = iss.readLine()
                                            if(a == null) break
                                            OS.write("[${server.name}]$a\r\n")
                                            OS.flush()
                                        }
                                    }
                                }
                            }
                        }else if (a.startsWith("forceStop ")){
                            val name = a.substring(10)
                            for(server in ConfigLoader.servers){
                                if(server.name==name){
                                    if(processMap[server]!!.isAlive){
                                        processMap[server]!!.destroyForcibly()
                                    }
                                }
                            }
                        }else if(a.startsWith("backup ")){
                            GlobalScope.launch {
                                val name=a.substring(7)
                                for(server in ConfigLoader.servers){
                                    if(server.name==name){
                                        val file=File("./backup/${server.name}")
                                        if(!file.exists()){
                                            file.mkdirs()
                                        }
                                        processMap[server]?.outputStream?.write("save-off\r\n".toByteArray(StandardCharsets.UTF_8))
                                        processMap[server]?.outputStream?.flush()
                                        processMap[server]?.outputStream?.write("save hold\r\n".toByteArray(StandardCharsets.UTF_8))
                                        processMap[server]?.outputStream?.flush()
                                        val mis=System.currentTimeMillis()
                                        compressDirectory("${server.workingDir}/./world",file,"$mis.zip")
                                        processMap[server]?.outputStream?.write("save-on\r\n".toByteArray(StandardCharsets.UTF_8))
                                        processMap[server]?.outputStream?.flush()
                                        processMap[server]?.outputStream?.write("save resume\r\n".toByteArray(StandardCharsets.UTF_8))
                                        processMap[server]?.outputStream?.flush()
                                        for(i in file.listFiles()){
                                            try {
                                                val a = i.canonicalPath.split("/").last().split("\\").last().replace(".zip", "").toLong()
                                                if (a <= mis - 10 * 24 * 3600 * 1000) {
                                                    i.delete()
                                                }
                                            }catch (e:NumberFormatException){
                                                continue
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (e: IOException) {
                    deal()
                }
            }.start()
            for(i in ConfigLoader.servers){
                GlobalScope.launch {
                    BufferedWriter(OutputStreamWriter(processMap[i]?.outputStream, StandardCharsets.UTF_8))
                    var iss = BufferedReader(InputStreamReader(processMap[i]?.inputStream, StandardCharsets.UTF_8))
                    while(true){
                        var a = iss.readLine()
                        if(a == null) break
                        OS.write("[${i.name}]$a\r\n")
                        OS.flush()
                    }
                }
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
            Thread{
                Thread.sleep(24*3600*1000)
                for (server in ConfigLoader.servers){
                   val file=File("./backup/${server.name}")
                   if(!file.exists()){
                       file.mkdirs()
                   }
                    processMap[server]?.outputStream?.write("save-off\r\n".toByteArray(StandardCharsets.UTF_8))
                    processMap[server]?.outputStream?.flush()
                    processMap[server]?.outputStream?.write("save hold\r\n".toByteArray(StandardCharsets.UTF_8))
                    processMap[server]?.outputStream?.flush()
                    val mis=System.currentTimeMillis()
                    compressDirectory("${server.workingDir}/./world",file,"$mis.zip")
                    processMap[server]?.outputStream?.write("save-on\r\n".toByteArray(StandardCharsets.UTF_8))
                    processMap[server]?.outputStream?.flush()
                    processMap[server]?.outputStream?.write("save resume\r\n".toByteArray(StandardCharsets.UTF_8))
                    processMap[server]?.outputStream?.flush()
                    for(i in file.listFiles()){
                        try {
                            val a = i.canonicalPath.split("/").last().split("\\").last().replace(".zip", "").toLong()
                            if (a <= mis - 10 * 24 * 3600 * 1000) {
                                i.delete()
                            }
                        }catch (e:NumberFormatException){
                            continue
                        }
                    }
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
fun compressDirectory(dir1: String, dir2: File, name: String) {
    val sourceDir = File(dir1)
    require(sourceDir.exists() && sourceDir.isDirectory) { "Source directory $dir1 is invalid" }
    val tempDir = createTempDir("zip_temp_").apply { deleteOnExit() }
    val tempTargetDir = File(tempDir, sourceDir.name).apply { mkdirs() }

    try {
        copyDirectorySkippingLockFiles(sourceDir, tempTargetDir)
        val zipFile = File(dir2, "$name.zip")
        createZipFile(tempTargetDir, zipFile)
    } finally {
        tempDir.deleteRecursively()
    }
}

private fun copyDirectorySkippingLockFiles(source: File, target: File) {
    source.listFiles()?.forEach { file ->
        if (file.name.endsWith(".lock", ignoreCase = true)) {
            return@forEach
        }

        val targetFile = File(target, file.name)
        when {
            file.isDirectory -> {
                targetFile.mkdirs()
                copyDirectorySkippingLockFiles(file, targetFile)
            }
            file.isFile -> {
                try {
                    file.copyTo(targetFile, overwrite = true)
                } catch (_:Exception){}
            }
        }
    }
}

private fun createZipFile(sourceDir: File, zipFile: File) {
    ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
        sourceDir.walk().forEach { file ->
            if (file == sourceDir) return@forEach

            val relativePath = sourceDir.toPath().relativize(file.toPath()).toString()
            val zipEntry = ZipEntry(
                relativePath + if (file.isDirectory) "/" else ""
            )

            try {
                zipOut.putNextEntry(zipEntry)

                if (file.isFile) {
                    FileInputStream(file).use { input ->
                        input.copyTo(zipOut)
                    }
                }

                zipOut.closeEntry()
            } catch (_: Exception) {}
        }
    }
}
//TODO test reload