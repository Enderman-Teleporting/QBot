package io.github.et

import io.github.et.ConfigLoader.load
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.net.Socket
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.system.exitProcess


object SubMain {
    private val processMap: MutableMap<MCServer, Process> = ConcurrentHashMap()
    private var bot: Process? = null
    lateinit var OS:BufferedWriter
    var QBotRunPathName: String? = null
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        try {
            val port = args[0].toInt()
            val s = Socket("127.0.0.1", port)
            val `is` = BufferedReader(InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8))
            OS = BufferedWriter(OutputStreamWriter(s.getOutputStream(),StandardCharsets.UTF_8))
            load()
            val root = File(".")
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
                                    val bw=BufferedWriter(OutputStreamWriter(processMap[server]!!.outputStream))
                                    bw.write(cmd)
                                    bw.newLine()
                                    bw.flush()
                                }
                            }
                        }else if(a.contains("<".toRegex())&&a.contains(">".toRegex())&&(!a.contains("\\[Server]".toRegex()))&&(!a.contains("/[a-z]+".toRegex()))){
                            val name= getContent(a)
                            for(server in ConfigLoader.servers){
                                if(server.name==name){
                                    val bw=BufferedWriter(OutputStreamWriter(processMap[server]!!.outputStream))
                                    bw.write("say ${a.substring(a.indexOf("<"))}")
                                    bw.newLine()
                                    bw.flush()
                                }
                            }
                        }else if(a.startsWith("restart ")) {
                            val name = a.substring(8)
                            for (server in ConfigLoader.servers) {
                                if (server.name == name) {
                                    GlobalScope.launch {
                                        val bw=BufferedWriter(OutputStreamWriter(processMap[server]!!.outputStream))
                                        bw.write("stop")
                                        bw.newLine()
                                        bw.flush()
                                        Thread.sleep(1000)
                                        if (processMap[server]!!.isAlive) {
                                            processMap[server]!!.destroy()
                                        }
                                        processMap[server] =
                                            ProcessBuilder(server.command.split(" ")).directory(File(server.workingDir))
                                                .start()
                                        BufferedWriter(
                                            OutputStreamWriter(
                                                processMap[server]?.outputStream,
                                                StandardCharsets.UTF_8
                                            )
                                        )
                                        var iss = BufferedReader(
                                            InputStreamReader(
                                                processMap[server]?.inputStream,
                                                StandardCharsets.UTF_8
                                            )
                                        )
                                        while (true) {
                                            if (!(processMap[server] ?: return@launch).isAlive) {
                                                break
                                            }
                                            try {
                                                var b = iss.readLine()
                                                if (b == null) continue
                                                OS.write("[${server.name}]$b\r\n")
                                                OS.flush()
                                            } catch (_: Exception) {
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(a.startsWith("forceStop ")){
                            val name=a.substring(10)
                            for(server in ConfigLoader.servers){
                                if(server.name==name) {
                                    if (processMap[server]!!.isAlive) {
                                        processMap[server]?.destroyForcibly()
                                    }
                                }
                            }
                        }else if(a.startsWith("startup ")) {
                            val name = a.substring(8)
                            for (server in ConfigLoader.servers) {
                                if (server.name == name) {
                                    if (!processMap[server]!!.isAlive) {
                                        GlobalScope.launch {
                                            processMap[server] =
                                                ProcessBuilder(server.command.split(" ")).directory(File(server.workingDir))
                                                    .start()
                                            BufferedWriter(
                                                OutputStreamWriter(
                                                    processMap[server]?.outputStream,
                                                    StandardCharsets.UTF_8
                                                )
                                            )
                                            var iss = BufferedReader(
                                                InputStreamReader(
                                                    processMap[server]?.inputStream,
                                                    StandardCharsets.UTF_8
                                                )
                                            )
                                            while (true) {
                                                if (!(processMap[server] ?: return@launch).isAlive) {
                                                    break
                                                }
                                                try {
                                                    var b = iss.readLine()
                                                    if (b == null) continue
                                                    OS.write("[${server.name}]$b\r\n")
                                                    OS.flush()
                                                } catch (_: Exception) {
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(a.startsWith("backup ")){
                            val name=a.substring(7)
                            for(server in ConfigLoader.servers){
                                if(name==server.name){
                                    GlobalScope.launch {
                                        val time=System.currentTimeMillis()
                                        val toDir=File("./backups/${server.name}")
                                        if(!toDir.exists()){
                                            toDir.mkdirs()
                                        }
                                        val bw=BufferedWriter(OutputStreamWriter(processMap[server]!!.outputStream))
                                        bw.write("save-off")
                                        bw.newLine()
                                        bw.flush()
                                        Thread.sleep(300)
                                        compressDirectory(server.workingDir + "/world", toDir, time.toString())
                                        bw.write("save-on")
                                        bw.newLine()
                                        bw.flush()
                                        Thread.sleep(300)
                                        for (i in toDir.listFiles()){
                                            if(i.name.endsWith(".zip")&&i.name.substring(0,i.name.length -4).matches("[0-9]+".toRegex())){
                                                if(i.name.substring(0,i.name.length -4).toLong()+24*3600*1000*10<=time){
                                                    i.delete()
                                                }
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
                        if(!(processMap[i] ?: return@launch).isAlive){
                            break
                        }
                        try {
                            var a = iss.readLine()
                            if (a == null) continue
                            OS.write("[${i.name}]$a\r\n")
                            OS.flush()
                        }catch (_:Exception){}
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
        Thread {
            while (true) {
                if (SimpleDateFormat("HH:mm:ss").format(Date()) == "00:00:00" || SimpleDateFormat("HH:mm:ss").format(
                        Date()
                    ) == "00:00:01"
                ) {
                    for (server in ConfigLoader.servers) {
                        GlobalScope.launch {
                            val time = System.currentTimeMillis()
                            val toDir = File("./backups/${server.name}")
                            if (!toDir.exists()) {
                                toDir.mkdirs()
                            }
                            val bw=BufferedWriter(OutputStreamWriter(processMap[server]!!.outputStream))
                            bw.write("save-off")
                            bw.newLine()
                            bw.flush()
                            Thread.sleep(300)
                            compressDirectory(server.workingDir + "/world", toDir, time.toString())
                            bw.write("save-on")
                            bw.newLine()
                            bw.flush()
                            for (i in toDir.listFiles()) {
                                if (i.name.endsWith(".zip") && i.name.substring(0, i.name.length - 4)
                                        .matches("[0-9]+".toRegex())
                                ) {
                                    if (i.name.substring(0, i.name.length - 4)
                                            .toLong() + 24 * 3600 * 1000 * 10 <= time
                                    ) {
                                        i.delete()
                                    }
                                }
                            }
                        }
                    }
                    Thread.sleep(1000)
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