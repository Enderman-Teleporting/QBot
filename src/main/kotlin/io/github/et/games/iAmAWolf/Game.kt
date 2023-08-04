package io.github.et.games.iAmAWolf

data class Game(var playerNum:Int){
    var currentWolfNumber:Int=0
    var currentGoodNumber:Int=0
    var host:Player?=null
    var players:ArrayList<Player> =ArrayList()
    var wolf:Int = 0
    var doubleClawDoubleWolfCrow:Int = 0
    var invisibleWolf:Int = 0
    var gargoyle:Int = 0
    var wolfKing:Int = 0
    var whiteWolfKing:Int = 0
    var villager:Int = 0
    var guardian:Int = 0
    var witch:Int = 0
    var predictor:Int = 0
    var fool:Int = 0
    var hunter:Int = 0
    var knight:Int = 0
    var currentStatus=Status.PRE_GAME
    var wolfMembers:ArrayList<Player> =ArrayList()
    var doubleClawDoubleWolfCrowMembers:ArrayList<Player> =ArrayList()
    var invisibleWolfMembers:ArrayList<Player> =ArrayList()
    var gargoyleMembers:ArrayList<Player> =ArrayList()
    var wolfKingMembers:ArrayList<Player>?=ArrayList()
    var whiteWolfKingMembers:ArrayList<Player> =ArrayList()
    var villagerMembers:ArrayList<Player> =ArrayList()
    var guardianMembers:ArrayList<Player> =ArrayList()
    var witchMembers:ArrayList<Player> =ArrayList()
    var predictorMembers:ArrayList<Player> =ArrayList()
    var foolMembers:ArrayList<Player> =ArrayList()
    var hunterMembers:ArrayList<Player> = ArrayList()
    var knightMembers: ArrayList<Player> = ArrayList()
    var currentPolice:Player? = null

    fun resetGame(){
        currentWolfNumber=0
        currentGoodNumber=0
        host=null
        playerNum=0
        players=ArrayList()
        wolf = 0
        doubleClawDoubleWolfCrow= 0
        invisibleWolf= 0
        gargoyle = 0
        wolfKing= 0
        whiteWolfKing= 0
        villager= 0
        guardian = 0
        witch = 0
        predictor= 0
        fool= 0
        hunter = 0
        knight = 0
        currentStatus=Status.PRE_GAME
        players= ArrayList()
        wolfMembers=ArrayList()
        doubleClawDoubleWolfCrowMembers=ArrayList()
        invisibleWolfMembers=ArrayList()
        gargoyleMembers=ArrayList()
        wolfKingMembers=ArrayList()
        whiteWolfKingMembers=ArrayList()
        villagerMembers=ArrayList()
        guardianMembers=ArrayList()
        witchMembers=ArrayList()
        predictorMembers=ArrayList()
        foolMembers=ArrayList()
        hunterMembers= ArrayList()
        knightMembers= ArrayList()
        currentPolice=null
    }
}