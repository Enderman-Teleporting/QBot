package io.github.et.games.wordle

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

object Logic {
    fun generateWord(length: Int): ArrayList<String> {
        val words = mutableListOf<Pair<String, String>>()
        val inputStream = Logic::class.java.classLoader.getResourceAsStream("io/github/et/EnWords.csv")
        if (inputStream != null) {
            BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                reader.lineSequence().forEach { line ->
                    if (line.isNotEmpty()) {
                        val parts = line.split("\",\"", limit = 2)
                        if (parts.size == 2) {
                            val word = parts[0].removePrefix("\"")
                            val meaning = parts[1].removeSuffix("\"")
                            if (word.length == length) {
                                words.add(Pair(word, meaning))
                            }
                        }
                    }
                }
            }
        }
        val randomIndex = Random.nextInt(words.size)
        val selectedWord = words[randomIndex]
        
        return arrayListOf(selectedWord.first, selectedWord.second)
    }
    
    fun check(word: String): Boolean {
        val inputStream = Logic::class.java.classLoader.getResourceAsStream("io/github/et/Words.txt")
        if (inputStream != null) {
            BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                reader.lineSequence().forEach { line ->
                    if (line.trim().equals(word, ignoreCase = true)) {
                        return true
                    }
                }
            }
        }
        return false
    }
    
    fun match(ori: String, inp: String): String {
        val original= ori.lowercase(Locale.getDefault())
        val input = inp.lowercase(Locale.getDefault())
        val result = ArrayList<String>()
        val originalLength = original.length
        val inputLength = input.length
        repeat(inputLength) { result.add("\uD83D\uDFEB") }
        val originalCharCount = mutableMapOf<Char, Int>()
        for (char in original) {
            originalCharCount[char] = originalCharCount.getOrDefault(char, 0) + 1
        }
        val usedCharCount = mutableMapOf<Char, Int>()
        for (i in input.indices) {
            if (input[i] == original[i]) {
                result[i] = "\uD83D\uDFE9"
                usedCharCount[input[i]] = usedCharCount.getOrDefault(input[i], 0) + 1
            }
        }
        for (i in input.indices) {
            if (result[i] == "\uD83D\uDFEB") {
                val currentChar = input[i]
                val totalInOriginal = originalCharCount.getOrDefault(currentChar, 0)
                val alreadyUsed = usedCharCount.getOrDefault(currentChar, 0)
                if (alreadyUsed < totalInOriginal) {
                    result[i] = "\uD83D\uDFE8"
                    usedCharCount[currentChar] = alreadyUsed + 1
                }
            }
        }
        
        return result.joinToString(separator = "")
    }
}