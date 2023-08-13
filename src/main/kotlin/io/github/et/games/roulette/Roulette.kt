package io.github.et.games.roulette

import io.github.et.exceptions.GameCrashedException
import io.github.et.games.Player
import io.github.ettoolset.tools.logger.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import kotlin.coroutines.CoroutineContext


@Suppress("unused")
class Roulette: SimpleListenerHost() {
    private val games = mutableMapOf<Long, Game>()
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        val logger:Logger=Logger.getDeclaredLogger()
        logger.error("Game Roulette crashed, info as below")
        throw GameCrashedException("Game Roulette crashed")
    }

    @EventHandler
    suspend fun GroupMessageEvent.onGameStart(): ListeningStatus {
        val game = games.getOrPut(group.id) { Game(null, null, null, null) }

        when {
            message.contentToString().startsWith("俄罗斯轮盘") -> {
                val bulletCount = message.contentToString().split(" ")[1].toIntOrNull()
                if(bulletCount==null){
                    group.sendMessage("非法子弹数量")
                    return ListeningStatus.LISTENING
                }
                if (bulletCount in 1..5) {
                    val chain: MessageChain= buildMessageChain {
                        add(At(sender.id))
                        +PlainText("发起了挑战！输入“接受挑战”以加入！")
                    }
                    subject.sendMessage(chain)
                    game.isGameRunning=true
                    game.player1=Player(group.id,sender.id)
                    game.gun=Gun(bulletCount)
                    game.bulletArray= game.gun!!.initialize()
                    launch{
                        delay(30000)
                        if (game.player2 == null) {
                            game.resetGame()
                        }
                    }
                    return ListeningStatus.LISTENING
                }else{
                    sender.sendMessage("子弹数量只能是1到5,一共也才6个子弹槽")
                }
            }
            message.contentToString()=="接受挑战"->{
                if (game.isGameRunning && game.player1 != null && game.player2 == null) {
                    game.player2 = Player(group.id, sender.id)
                    val chain= buildMessageChain {
                        add(At(game.player2!!.playerId))
                        +PlainText(" 接受了挑战, ")
                        add(At(game.player1!!.playerId))
                        +PlainText("，开枪吧！")
                    }
                    group.sendMessage(chain)
                    game.currentPlayer = game.player1
                    game.round=0
                    return ListeningStatus.LISTENING
                }
            }
            message.contentToString()=="开枪"->{
                if (game.isGameRunning && game.player1 != null && game.player2 != null) {
                    if (sender.id == game.currentPlayer!!.playerId) {
                        if (game.bulletArray[game.round]!!) {
                            var chain= buildMessageChain {
                                add(At(game.currentPlayer!!.playerId))
                                +PlainText("崩！你寄了")
                            }
                            group.sendMessage(chain)
                            chain= buildMessageChain {
                                add(At((if (game.currentPlayer==game.player1)game.player2 else game.player1)!!.playerId))
                                +PlainText("是本场获胜者！")
                            }
                            group.sendMessage(chain)
                            var bulletArrange:String=""
                            for (i in game.bulletArray){
                                bulletArrange += (if(i!!)"1" else{"0"})
                            }
                            group.sendMessage("本场子弹排布$bulletArrange")
                            game.resetGame()
                            return ListeningStatus.LISTENING
                        } else {
                            var chain= buildMessageChain {
                                add(At(game.currentPlayer!!.playerId))
                                +PlainText("你很幸运，没有寄")
                            }
                            subject.sendMessage(chain)
                            game.currentPlayer = if (game.currentPlayer == game.player1) game.player2 else game.player1
                            chain= buildMessageChain {
                                add(At(game.currentPlayer!!.playerId))
                                +PlainText("轮到你了")
                            }
                            group.sendMessage(chain)
                            return ListeningStatus.LISTENING
                        }
                    }
                }
            }
            else->{
                return ListeningStatus.LISTENING
            }
        }


        return ListeningStatus.LISTENING
    }
}