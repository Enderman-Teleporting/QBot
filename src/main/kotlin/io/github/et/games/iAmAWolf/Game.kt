package io.github.et.games.iAmAWolf

data class Game(var playerNum:Int){
    var players:Array<Player>?=null
    var isGameRunning:Boolean = false
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
    var currentStatus=Status.GETTING_READY
    var wolfMembers:Array<Player>?=null
    var doubleClawDoubleWolfCrowMembers:Array<Player>?=null
    var invisibleWolfMembers:Array<Player>?=null
    var gargoyleMembers:Array<Player>?=null
    var wolfKingMembers:Array<Player>?=null
    var whiteWolfKingMembers:Array<Player>?=null
    var villagerMembers:Array<Player>?=null
    var guardianMembers:Array<Player>?=null
    var witchMembers:Array<Player>?=null
    var predictorMembers:Array<Player>?=null
    var foolMembers:Array<Player>? =null
    var hunterMembers:Array<Player>? = null
    var knightMembers: Array<Player>? = null
    var currentPolice:Player? = null

    fun resetGame(){
        playerNum=0
        players=null
        isGameRunning= false
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
        currentStatus=Status.GETTING_READY
        players= emptyArray()
        wolfMembers=null
        doubleClawDoubleWolfCrowMembers=null
        invisibleWolfMembers=null
        gargoyleMembers=null
        wolfKingMembers=null
        whiteWolfKingMembers=null
        villagerMembers=null
        guardianMembers=null
        witchMembers=null
        predictorMembers=null
        foolMembers=null
        hunterMembers= null
        knightMembers= null
        currentPolice=null
    }
}