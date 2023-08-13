package io.github.et.games.roulette

import kotlin.random.Random
import kotlin.random.asJavaRandom

open class Gun (private val bulletNumber: Int){
    open fun initialize(): Array<Boolean?> {
        val random: java.util.Random = Random.asJavaRandom()
        val bulletArray: Array<Boolean?> = arrayOfNulls(6)
        for (i in 0..5){
            bulletArray[i]=false
        }
        var trues=0
        while (trues<bulletNumber){
            trues+=1
            var ran=random.nextInt(0,6)
            while (bulletArray[ran]!!){
                ran=random.nextInt(0,6)
            }
            bulletArray[ran]=true
        }
        return bulletArray
    }
}