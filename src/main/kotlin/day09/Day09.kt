package day09

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val input = Path("input/day09.txt").readLines()

    val sequences = input.map { it.splitInts() }

    val part1 = sequences.map { nextVal(it) }
    val part2 = sequences.map { previousVal(it) }

    println(part1.sum())
    println(part2.sum())
}

private fun String.splitInts(): List<Int> {
    return this.split(" ").map { c -> c.toInt() }
}

fun nextVal(sequence: List<Int>): Int {
    val nextRow = sequence.zipWithNext { a, b -> b - a }
    if (nextRow.all { it == 0 }) {
        return sequence.last()
    }
    return nextVal(nextRow) + sequence.last()
}


fun previousVal(sequence: List<Int>): Int {
    val nextRow = sequence.zipWithNext { a, b -> b - a }
    if (nextRow.all { it == 0 }) {
        return sequence.first()
    }
    return sequence.first() - previousVal(nextRow)
}