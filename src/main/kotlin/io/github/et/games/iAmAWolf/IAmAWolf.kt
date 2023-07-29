package io.github.et.games.iAmAWolf

import io.github.et.exceptions.GameCrashedException
import io.github.ettoolset.tools.logger.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.GroupMessageEvent
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
class IAmAWolf : SimpleListenerHost() {
    private val gameMap:MutableMap<Long,Game> =mutableMapOf()
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        val logger: Logger = Logger.getDeclaredLogger()
        logger.error("Game I am a wolf crashed, info as below")
        throw GameCrashedException("Game I am a wolf crashed")
    }
    @EventHandler
    suspend fun GroupMessageEvent.iAmAWolf():ListeningStatus{
        var game: Game =gameMap.getOrPut(group.id){Game(0)}
        when{
            message.contentToString().startsWith("狼人杀 ")->{
                if (game.currentStatus==Status.PRE_GAME) {
                    val msg:Int
                    try {
                        msg = message.contentToString().replace("狼人杀 ", "").toInt()
                    } catch (e: Exception) {
                        subject.sendMessage("非法游戏人数")
                        return ListeningStatus.LISTENING
                    }
                    if (msg<=3){
                        subject.sendMessage("最少四个人开始玩")
                        return ListeningStatus.LISTENING
                    }else{
                        game.host= Player(group.id,sender.id,false)
                        game.players.add(Player(group.id,sender.id,false))
                        game.currentStatus=Status.GETTING_READY
                        game.playerNum=msg
                        subject.sendMessage("""已开启${msg}人狼人杀游戏,请发起者按以下格式配制游戏:
                            队伍配置：狼人数量 狼鸦之爪数量 隐狼数量 石像鬼数量 狼王数量 白狼王数量 平民数量 守卫数量 女巫数量 预言家数量 白痴数量 猎人数量 骑士数量
                            每个数字之间写一个空格，中文冒号
                            例如：
                            队伍配置：1 2 0 0 0 0 3 1 1 1 0 0 1
                            """.trimIndent())
                        launch {
                            delay(1200000)
                            if(game.currentStatus!=Status.ENGAGING){
                                subject.sendMessage("两分钟了,等不及了,如果你还想玩的话重新开吧")
                                game.resetGame()
                            }
                        }
                        return ListeningStatus.LISTENING
                    }
                }else{
                    subject.sendMessage("有游戏正在运行或者已经有开始游戏申请了哦")
                    ListeningStatus.LISTENING
                }
            }
            message.contentToString().startsWith("队伍配置：")->{

            }
        }
        return ListeningStatus.LISTENING
    }
    
}