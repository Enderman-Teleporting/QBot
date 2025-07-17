package io.github.et.games.wordle

data class Game(var word:String?){
    var wordLength:Int=0
    var isGameRunning=false
    var meaning:String?=null
}