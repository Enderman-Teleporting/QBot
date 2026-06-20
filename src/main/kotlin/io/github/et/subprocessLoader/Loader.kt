package  io.github.et.subprocessLoader
import com.alibaba.fastjson2.JSONObject
import io.github.et.Main
import io.github.et.eventListener.AdminBuffet
import io.github.et.eventListener.ChangeGroupName
import io.github.et.eventListener.LeaverListener
import io.github.et.eventListener.RequestPasser
import io.github.et.exceptions.BotInfoNotFoundException
import io.github.et.games.roulette.Roulette
import io.github.et.games.wordle.Wordle
import io.github.et.messager.*
import io.github.et.tools.CommandConsole
import io.github.et.tools.DeathMessage
import io.github.et.utils.classLoader.ClassLoader
import io.github.ettoolset.tools.logger.LevelNotMatchException
import io.github.ettoolset.tools.logger.Logger
import io.github.ettoolset.tools.logger.LoggerNotDeclaredException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.ListenerHost
import top.mrxiaom.overflow.BotBuilder
import java.io.*
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
                if(a.trim().matches("^\\[]\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d \\[.*debug.*] 本账号数据/缓存目录： .+$".toRegex())){
                    GlobalScope.launch {
                        if(Main.bot!=null){
                            Main.bot.close()
                        }
                        val logger=Logger.getDeclaredLogger()
                        do {
                            Main.bot =
                                BotBuilder.positive("ws://127.0.0.1:" + (Main.JSON_ALL["Global"] as JSONObject)["port"])
                                    .connect()
                        }while (Main.bot==null)
                        Main.bot.login()
                        logger.info("正在注册监听器……")
                        val clazz = ClassLoader.loadClasses()
                        clazz.add(AdminBuffet::class.java)
                        clazz.add(ChangeGroupName::class.java)
                        clazz.add(LeaverListener::class.java)
                        clazz.add(RequestPasser::class.java)
                        clazz.add(ChangeConfigListener::class.java)
                        clazz.add(FreeTalk::class.java)
                        clazz.add(GetHelp::class.java)
                        clazz.add(ImageGenerator::class.java)
                        clazz.add(MinecraftServer::class.java)
                        clazz.add(Nudger::class.java)
                        clazz.add(Repeater::class.java)
                        clazz.add(Replier::class.java)
                        clazz.add(ServerSearcher::class.java)
                        clazz.add(Roulette::class.java)
                        clazz.add(Wordle::class.java)
                        clazz.add(Interactions::class.java)
                        clazz.add(MessageCount::class.java)
                        for (c in clazz) {
                            val abc = c.getDeclaredConstructor().newInstance()
                            if (abc is ListenerHost) {
                                Main.bot.eventChannel.registerListenerHost(abc)
                            }
                            logger.info("已注册监听器" + c.name)
                        }
                        Thread {
                            while (true) {
                                try {
                                    if (Main.bot == null) {
                                        continue
                                    }
                                    logger.fine(
                                        CommandConsole.handle(
                                            Main.bot,
                                            CommandConsole.getCommand()
                                        )
                                    )
                                } catch (e:Exception){
                                    break
                                }
                            }
                        }.start()
                        Main.bot.join()
                    }
                }
                if (name.isEmpty() || Main.bot == null) {
                    continue
                }
                for (i in ConfigLoader.servers) {
                    if (i.name == name) {
                        if (content.contains("<".toRegex()) && content.contains(">".toRegex())&&(!a.contains("\\[Server]".toRegex()))&&(!a.contains("<init>".toRegex()))){
                            GlobalScope.launch {
                                Objects.requireNonNull(Main.bot.getGroup(i.group))?.sendMessage("[" + name + "]" + content.substring(content.indexOf("<")))
                            }
                        }else if((content.trim().endsWith(" left the game")||content.endsWith(" joined the game"))&&(!content.contains("\\[Server]".toRegex()))&&(!content.contains("<init>".toRegex()))){
                            val aaa=content.split("[\\s:]".toRegex())
                            GlobalScope.launch {
                                Objects.requireNonNull(Main.bot.getGroup(i.group))?.sendMessage("[" + name + "]" + aaa[aaa.size-4]+" "+aaa[aaa.size-3]+" "+aaa[aaa.size-2]+" "+aaa[aaa.size-1])
                            }
                        }else {
                            val result=DeathMessage.getDeathMessage(content)
                            if(result!=null&&(!content.contains("\\[Server]".toRegex()))){
                                GlobalScope.launch {
                                    Objects.requireNonNull(Main.bot.getGroup(i.group))?.sendMessage("[$name]$result")
                                }
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
