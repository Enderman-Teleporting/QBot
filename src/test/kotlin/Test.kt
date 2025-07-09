import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun compressDirectory(dir1: String, dir2: File, name: String) {
    val sourceDir = File(dir1)
    require(sourceDir.exists() && sourceDir.isDirectory) { "Source directory $dir1 is invalid" }

    // 创建临时工作目录
    val tempDir = createTempDir("zip_temp_").apply { deleteOnExit() }
    val tempTargetDir = File(tempDir, sourceDir.name).apply { mkdirs() }

    try {
        // 复制目录并跳过lock文件
        copyDirectorySkippingLockFiles(sourceDir, tempTargetDir)

        // 创建目标ZIP文件
        val zipFile = File(dir2, "$name.zip")

        // 压缩临时目录内容
        createZipFile(tempTargetDir, zipFile)
    } finally {
        // 确保临时目录被删除
        tempDir.deleteRecursively()
    }
}

private fun copyDirectorySkippingLockFiles(source: File, target: File) {
    source.listFiles()?.forEach { file ->
        if (file.name.endsWith(".lock", ignoreCase = true)) {
            println("Skipping lock file: ${file.absolutePath}")
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
                } catch (e: FileSystemException) {
                    // 处理文件被锁定的情况
                    System.err.println("WARNING: Skipped locked file [${file.absolutePath}] - ${e.message}")
                } catch (e: AccessDeniedException) {
                    // 处理无权限访问的文件
                    System.err.println("WARNING: Skipped access-denied file [${file.absolutePath}] - ${e.message}")
                } catch (e: IOException) {
                    // 处理其他IO异常
                    System.err.println("WARNING: Skipped file [${file.absolutePath}] due to error: ${e.message}")
                }
            }
        }
    }
}

private fun createZipFile(sourceDir: File, zipFile: File) {
    ZipOutputStream(FileOutputStream(zipFile)).use { zipOut ->
        sourceDir.walk().forEach { file ->
            if (file == sourceDir) return@forEach  // 跳过根目录自身

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
            } catch (e: Exception) {
                System.err.println("WARNING: Failed to add file to ZIP [${file.absolutePath}] - ${e.message}")
            }
        }
    }
}
fun main(){
    val processBuilder = ProcessBuilder("D:\\.jdks\\openjdk-22.0.2\\bin\\java.exe","-jar","server.jar").directory(File("C:\\Users\\wyh\\Desktop\\dontjuanserver"))
    val process = processBuilder.start()
    val inputStream=process.inputStream
    val outputStream = process.outputStream
    while(true){
        val line = inputStream.bufferedReader().readLine()
        if(line==null)continue
        println(line)
        if(line.contains("Done")){
            outputStream.write("save-off\r\n".toByteArray(StandardCharsets.UTF_8))
            outputStream.flush()
            break
        }
    }

    compressDirectory("C:\\Users\\wyh\\Desktop\\dontjuanserver\\world",File("C:\\Users\\wyh\\Desktop"),"114514")
    outputStream.write("save-on\r\n".toByteArray(StandardCharsets.UTF_8))
    outputStream.flush()
}