package io.github.et.games.roulette

import io.github.et.games.Player

class Game(var player1: Player?, var player2: Player?, var currentPlayer: Player?,var gun: Gun?) {
    lateinit var bulletArray: Array<Boolean?>
    var round=0
    var isGameRunning = false

    fun resetGame() {
        gun=null
        bulletArray= emptyArray()
        player1 = null
        player2 = null
        currentPlayer = null
        isGameRunning = false
        round=0
    }
}