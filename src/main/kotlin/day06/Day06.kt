package day06

import java.io.File

fun main() {
    val input = File("input/day06.txt").readLines()

    val times = input[0].split("""\s+""".toRegex()).drop(1).map { it.toLong() }
    val distances = input[1].split("""\s+""".toRegex()).drop(1).map { it.toLong() }

    val results = mutableListOf<Long>()
    times.forEachIndexed { i, time ->
        results.add(raceBoat(time, distances[i]))
    }

    println(results.fold(1L) { acc, next -> acc * next })

    // Part two
    val time = input[0].split("""\s+""".toRegex()).drop(1).reduce { acc, s -> acc + s }.toLong()
    val distance = input[1].split("""\s+""".toRegex()).drop(1).reduce { acc, s -> acc + s }.toLong()

    println("time: $time, distance: $distance")
    println(raceBoat(time, distance))
}

fun raceBoat(time: Long, distance: Long): Long {
    var result = 0L

    for (t in 1..time) {
        val d = t * (time - t)
        if (d > distance) {
            result++
        }
    }

    return result
}