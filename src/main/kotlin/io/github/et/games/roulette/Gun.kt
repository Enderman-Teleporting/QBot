package io.github.et.games.roulette

import kotlin.random.Random
import kotlin.random.asJavaRandom

open class Gun (private val bulletNumber: Int){
    open fun initialize(): Array<Boolean?> {
        val random: java.util.Random = Random.asJavaRandom()
        val bulletArray: Array<Boolean?> = arrayOfNulls(6)
        var trues=0
        for (i:Int in bulletArray.indices){
            if(trues<bulletNumber){
                var isBullet:Boolean = random.nextInt(2)==1
                if(isBullet) {
                    trues += 1
                }
                bulletArray[i]=isBullet
            }
        }
        return bulletArray
    }
}