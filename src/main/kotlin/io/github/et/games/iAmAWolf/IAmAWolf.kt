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
import kotlin.random.Random

@Suppress("unused")
class IAmAWolf : SimpleListenerHost() {
    private val gameMap:MutableMap<Long,Game> =mutableMapOf()
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        val logger: Logger = Logger.getDeclaredLogger()
        logger.error("Game I am a wolf crashed, info as below")
        throw GameCrashedException("Game I am a wolf crashed")
    }
    @EventHandler
    suspend fun GroupMessageEvent.iAmAWolf() {
        /*var game: Game =gameMap.getOrPut(group.id){Game(0)}
        when{
            message.contentToString().startsWith("狼人杀 ")->{
                if (game.currentStatus==Status.PRE_GAME) {
                    if(!bot.friends.contains(sender.id)){
                        subject.sendMessage("你还不是我的好友哦 请先添加好友")
                        return ListeningStatus.LISTENING
                    }
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
                        game.host= Player(group.id,sender.id,false,true)
                        game.players.add(Player(group.id,sender.id,false,true))
                        game.currentStatus=Status.GETTING_READY
                        game.playerNum=msg
                        subject.sendMessage("""已开启${msg}人狼人杀游戏,请发起者按以下格式配制游戏:
                            队伍配置：狼人数量 狼鸦之爪数量 隐狼数量 石像鬼数量 狼王数量 白狼王数量 平民数量 守卫数量 女巫数量 预言家数量 白痴数量 猎人数量 骑士数量
                            每个数字之间写一个空格，中文冒号
                            例如：
                            队伍配置：1 2 0 0 0 0 3 1 1 1 0 0 1
                            """.trimIndent())
                        return ListeningStatus.LISTENING
                    }
                }else{
                    subject.sendMessage("有游戏正在运行或者已经有开始游戏申请了哦")
                    ListeningStatus.LISTENING
                }
            }
            message.contentToString().startsWith("队伍配置：")->{
                if (game.currentStatus==Status.GETTING_READY&&sender.id==game.host!!.playerId) {
                    try {
                        var teamArray=message.contentToString().replace("队伍配置：","").split(" ")
                        game.wolf=teamArray[0].toInt()
                        game.doubleClawDoubleWolfCrow=teamArray[1].toInt()
                        game.invisibleWolf=teamArray[2].toInt()
                        game.gargoyle=teamArray[3].toInt()
                        game.wolfKing=teamArray[4].toInt()
                        game.whiteWolfKing=teamArray[5].toInt()
                        game.villager=teamArray[6].toInt()
                        game.guardian=teamArray[7].toInt()
                        game.witch=teamArray[8].toInt()
                        game.predictor=teamArray[9].toInt()
                        game.fool=teamArray[10].toInt()
                        game.hunter=teamArray[11].toInt()
                        game.knight=teamArray[12].toInt()
                        game.currentWolfNumber=teamArray[0].toInt()+teamArray[1].toInt()+teamArray[2].toInt()+teamArray[3].toInt()+teamArray[4].toInt()+teamArray[5].toInt()
                        game.currentGoodNumber=teamArray[6].toInt()+teamArray[7].toInt()+teamArray[8].toInt()+teamArray[9].toInt()+teamArray[10].toInt()+teamArray[11].toInt()+teamArray[12].toInt()
                        if (game.currentWolfNumber<1||game.currentGoodNumber<2){
                            subject.sendMessage("你这配置咋玩啊 请重新开始游戏")
                            game.resetGame()
                            return ListeningStatus.LISTENING
                        }
                        subject.sendMessage("配置完成！发送“加入游戏”即可加入")
                        game.currentStatus=Status.ENGAGING
                    }catch(e:Exception){
                        subject.sendMessage("非法配置，请重新输入")
                        return ListeningStatus.LISTENING
                    }


                }


            }
            message.contentToString() == "加入游戏" ->{
                if(game.currentStatus==Status.ENGAGING) {
                    if (game.players.size < game.playerNum) {
                        if(bot.friends.contains(sender.id)){
                            game.players.add(Player(group.id, sender.id, false, true))
                            subject.sendMessage("加入成功")
                            for(i in 0..game.playerNum) {
                                while (true) {
                                    val randomPlayer = Random(114514L).nextInt(1, 13)
                                    when (randomPlayer) {
                                        1 -> {
                                            game.wolfMembers.add(Player(group.id, sender.id, false, true))
                                            if (game.wolfMembers.size > game.wolf) {
                                                game.wolfMembers.removeLast()

                                            }
                                            else{
                                                game.players[i].identity="wolf"
                                                break
                                            }
                                        }
                                        2->{
                                            game.doubleClawDoubleWolfCrowMembers.add(Player(group.id,sender.id,false,true))
                                            if(game.doubleClawDoubleWolfCrowMembers.size>game.doubleClawDoubleWolfCrow){
                                                game.doubleClawDoubleWolfCrowMembers.removeLast()
                                            }
                                            else{
                                                game.players[i].identity="dCDWC"
                                                break
                                            }
                                        }
                                        3->{
                                            game.invisibleWolfMembers.add(Player(group.id,sender.id,false,true))
                                            if(game.invisibleWolfMembers.size>game.invisibleWolf){
                                                game.invisibleWolfMembers.removeLast()
                                            }
                                            else{
                                                game.players[i].identity="invisibleWolf"
                                                break
                                            }
                                        }
                                        4->{
                                            game.gargoyleMembers.add(Player(group.id,sender.id,false,true))
                                            if(game.gargoyleMembers.size>game.gargoyle){
                                                game.gargoyleMembers.removeLast()
                                            }
                                            else{
                                                game.players[i].identity="gargoyle"
                                                break
                                            }
                                        }
                                        5->{
                                            game.wolfKingMembers.add(Player(group.id,sender.id,false,true))
                                            if(game.wolfKingMembers.size >game.wolfKing){
                                                game.wolfKingMembers.removeLast()
                                            }
                                            else{
                                                break
                                            }
                                        }
                                        6->{
                                            game.whiteWolfKingMembers.add(Player(group.id,sender.id,false,true))
                                            if(game.wolfKingMembers.size >game.wolfKing){
                                                game.wolfKingMembers.removeLast()
                                            }
                                            else{
                                                break
                                            }
                                        }
                                        7->{
                                            game.villagerMembers.add(Player(group.id,sender.id,false,true))
                                            if(game.villagerMembers.size>game.villager){
                                                game.villagerMembers.removeLast()
                                            }
                                        }
                                        8->{
                                            game.guardianMembers.add(Player(group.id,sender.id,false,true))
                                        }
                                        9->{
                                            game.witchMembers
                                        }
                                        10->{
                                            game.
                                        }
                                    }
                                }
                            }
                            TODO("character generate, game start announcement, status set")
                            return ListeningStatus.LISTENING
                        } else{
                            subject.sendMessage("你还不是我好友哦，玩的时候是要有私聊消息的")
                            return ListeningStatus.LISTENING
                        }
                    } else {
                        subject.sendMessage("游戏满人了哦")
                        return ListeningStatus.LISTENING
                    }
                }

            }
        }
        return ListeningStatus.LISTENING*/
    }

    
}