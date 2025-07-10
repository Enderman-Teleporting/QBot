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
                } catch (e:Exception){

                }
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
//基岩save hold/ save resume