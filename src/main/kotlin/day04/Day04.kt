package day04

import java.io.File

fun main() {
    val input = File("input/day04.txt").readLines()

//    val result = input.map { processLine(it) }.fold(0){acc, next -> acc + next}
//
//    println(result)

    val totalCards = processDuplicates(input)

    println(totalCards)
}

fun processLine(line: String): Int {
    val (_, numbers) = line.split(":")
    val (winning, mine) = numbers.split(" | ")

    val winnerSet = winning.trim().split("""\s+""".toRegex()).map { it.toDouble() }.toSet()
    val mySet = mine.trim().split("""\s+""".toRegex()).map { it.toDouble() }.toSet()

    val winners = winnerSet.intersect(mySet)

    //return winners.fold(0.5){ acc, next -> (acc * 2)}.toInt()
    return winners.size
}

fun processDuplicates(input: List<String>): Int {
    val winners = input.mapIndexed { index, line -> index to processLine(line) }.toMap()
    val cardTotals = input.indices.associateWith { 1 }.toMutableMap()

    winners.forEach { (index, count) -> run {
        for (j in 1 ..cardTotals[index]!!) {
            for (i in 1 .. count) {
                cardTotals[index + i] = cardTotals[index + i]!! + 1
            }
        }
    }}

    return cardTotals.values.fold(0){acc, next -> acc + next}
}

operator fun Int?.times(other: Int) = this?.times(other) ?: other
operator fun Int?.plus(other: Int) = this?.plus(other) ?: other
