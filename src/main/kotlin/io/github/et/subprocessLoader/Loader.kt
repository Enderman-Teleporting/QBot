package  io.github.et.subprocessLoader
import io.github.et.Main
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*


class Loader : Runnable {
    @OptIn(DelicateCoroutinesApi::class)
    override fun run() {
        try {
            ConfigLoader.load()
            val serverStream = ServerStream()
            val `is` = BufferedReader(InputStreamReader(ServerStream.`is`, StandardCharsets.UTF_8))
            val os = BufferedWriter(OutputStreamWriter(ServerStream.os, StandardCharsets.UTF_8))
            while (true) {
                val a = `is`.readLine() ?: continue
                val name = getContent(a)
                val content = a.substring(name.length + 2)
                System.out.println(a.replaceFirst("[]", ""))
                if (name.isEmpty() || Main.bot == null) {
                    continue
                }
                for (i in ConfigLoader.servers) {
                    if (i.name == name) {
                        if (content.contains("<".toRegex()) && content.contains(">".toRegex())&&(!a.contains("\\[Server]".toRegex()))) {
                            GlobalScope.launch {
                                Objects.requireNonNull(Main.bot.getGroup(i.group))?.sendMessage("[" + name + "]" + content.substring(content.indexOf("<")))
                            }
                        }else if(content.trim().endsWith(" left the game")||content.endsWith(" joined the game")){
                            val aaa=content.split("[\\s:]".toRegex())
                            GlobalScope.launch {
                                Objects.requireNonNull(Main.bot.getGroup(i.group))?.sendMessage("[" + name + "]" + aaa[aaa.size-4]+" "+aaa[aaa.size-3]+" "+aaa[aaa.size-2]+" "+aaa[aaa.size-1])
                            }
                        }
                    }
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    companion object {
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


    }
}
