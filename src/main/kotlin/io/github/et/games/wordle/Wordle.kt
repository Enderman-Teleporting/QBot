package io.github.et.games.wordle

import io.github.et.exceptions.GameCrashedException
import io.github.et.utils.json.FeatureInUse
import io.github.ettoolset.tools.logger.Logger
import net.mamoe.mirai.event.EventHandler
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.events.MessageEvent
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext


@Suppress("unused")
class Wordle: SimpleListenerHost() {
    companion object {
        var GameRecord: ConcurrentHashMap<Long, ArrayList<String>> = ConcurrentHashMap<Long, ArrayList<String>>()
        var GameMap: MutableMap<Long, Game> = mutableMapOf()
    }
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        val logger: Logger = Logger.getDeclaredLogger()
        logger.error("Game Wordle crashed, info as below")
        throw GameCrashedException("Game Wordle crashed")
    }

    @EventHandler
    suspend fun MessageEvent.wordleMsg(){
        if(FeatureInUse.isInUse("Wordle",subject.id)){
            if(message.contentToString().trim().matches("wordle [0-9]+".toRegex())){
                if (!GameMap.contains(subject.id)) {
                    GameMap[subject.id] = Game(null)
                }
                if(!GameRecord.contains(subject.id)){
                    GameRecord[subject.id] = ArrayList()

                }
                if (!GameMap[subject.id]!!.isGameRunning){
                    if(message.contentToString().substring(7).matches("[0-9]+".toRegex())){
                        if(message.contentToString().substring(7).toInt() in 4..11){
                            GameMap[subject.id]!!.wordLength= message.contentToString().substring(7).toInt()
                        }else {
                            subject.sendMessage("请输入一个4-11之间的数字")
                            return
                        }
                    }
                    GameRecord[subject.id]!!.clear()
                    GameMap[subject.id]!!.isGameRunning= true
                    subject.sendMessage("请输入你的第一个猜测!")
                    val word= Logic.generateWord(GameMap[subject.id]!!.wordLength)
                    GameMap[subject.id]!!.word = word[0]
                    GameMap[subject.id]!!.meaning=word[1]
                }else{
                    subject.sendMessage("请先完成当前游戏!")
                }
            }else if(message.contentToString().matches("[a-zA-Z]+".toRegex())){
                if(GameMap.contains(subject.id)){
                    if(GameMap[subject.id]!!.isGameRunning&&GameMap[subject.id]!!.wordLength==message.contentToString().length){
                        val guess= message.contentToString()
                        if(Logic.check(guess)){
                            GameRecord[subject.id]!!.add(guess)
                            val sb=StringBuilder()
                            for(i in 0 until GameRecord[subject.id]!!.size-1){
                                sb.append(GameRecord[subject.id]!![i]).append("\n").append(Logic.match(GameMap[subject.id]!!.word!!,GameRecord[subject.id]!![i])).append("\n")
                            }
                            val last= Logic.match(GameMap[subject.id]!!.word!!,guess)
                            sb.append(guess).append("\n").append(Logic.match(GameMap[subject.id]!!.word!!,guess))
                            subject.sendMessage(sb.toString())
                            if(last.matches("\uD83D\uDFE9+".toRegex())){
                                subject.sendMessage("恭喜你猜对了！")
                                subject.sendMessage("该单词意思是:"+GameMap[subject.id]!!.meaning)
                                GameMap[subject.id]!!.word=null
                                GameMap[subject.id]!!.isGameRunning=false
                            }else{
                                if(GameRecord[subject.id]!!.size>=6){
                                    subject.sendMessage("很遗憾，你没有猜对")
                                    subject.sendMessage("正确答案为:"+GameMap[subject.id]!!.word+", 意为"+GameMap[subject.id]!!.meaning)
                                    GameMap[subject.id]!!.word=null
                                    GameMap[subject.id]!!.isGameRunning=false
                                }
                            }
                        }else{
                            subject.sendMessage("该单词不存在")
                        }
                    }
                }
            }
        }else{
            subject.sendMessage("功能未开启!")
        }
    }
}