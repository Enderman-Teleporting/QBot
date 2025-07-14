import io.github.et.ConfigLoader
import io.github.et.compressDirectory
import java.io.*

class TestMain {
}

fun main() {
    ConfigLoader.load()
    val process =
        ProcessBuilder(ConfigLoader.servers.get(0).command.split(" ")).directory(File(ConfigLoader.servers.get(0).workingDir)).start()
    val o=process.outputStream
    val i=process.inputStream
    val bo=BufferedWriter(OutputStreamWriter(o))
    val bi = BufferedReader(InputStreamReader(i))
    val file=File("./backup")
    if(!file.exists()){
        file.mkdirs()
    }
    while(true){
        if (bi.readLine() == null) {
            continue
        }else{
            var a=bi.readLine()
            println(a)
            if(a.contains("For help")||a.contains("Time elapsed")){
                bo.write("save-off")
                bo.newLine()
                bo.flush()
                compressDirectory(ConfigLoader.servers.get(0).workingDir+"/world/",file,"114514")
                bo.write("save-on")
                bo.newLine()
                bo.flush()
            }
        }
    }



}