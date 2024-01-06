package day14

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val dish = Path("input/day14.txt").readLines()
    val input = dish.map { it.toCharArray() }.toTypedArray()

    val tilted = mutableListOf<String>()
    for (col in input.first().indices) {
        val column = input.map { line -> line[col] }.toTypedArray().toCharArray().joinToString("")

        tilted.add( column.split("#").map { it.toCharArray().sortedDescending().toCharArray().joinToString("") }.joinToString("#") )
    }

    var load = tilted.map { col -> col.reversed().foldIndexed(0) { index, acc, next -> if (next == 'O') acc + index + 1 else acc  } }.sum()

    println(load)

    var cycled = dish
    val states = mutableListOf<List<String>>()
    val cycles = 1000000000
    var repeatIndex = 0
    for (i in 1..cycles) {
        cycled = tiltCycle(cycled)
        if (states.contains(cycled)) {
            repeatIndex = states.indexOf(cycled)
            break
        }
        states.add(cycled)
    }

    val loads = states.drop(repeatIndex).map { state ->
        state.foldIndexed(0L) { index, acc, next -> acc + (state.size - index) * next.count { it == 'O' } }
    }

    val loadIndex = ((1000000000 - repeatIndex) % 7) - 1

    println(loads[loadIndex])
}

fun tiltCycle(dish: List<String>): List<String> {
    // tilt north
    val doNorthTilt = mutableListOf<String>()
    for (c in dish.first().indices) {
        val column = dish.map { line -> line[c] }.toTypedArray().toCharArray().joinToString("")

        doNorthTilt.add( column.split("#").map { it.toCharArray().sortedDescending().toCharArray().joinToString("") }.joinToString("#") )
    }

    val tiltedNorth = mutableListOf<String>()
    for (c in doNorthTilt.indices) {
        val row = doNorthTilt.map { line -> line[c] }.toTypedArray().toCharArray().joinToString("")
        tiltedNorth.add(row)
    }

    // tilt west
    val tiltedWest = mutableListOf<String>()
    for (row in tiltedNorth) {
        tiltedWest.add( row.split("#").map { it.toCharArray().sortedDescending().toCharArray().joinToString("") }.joinToString("#") )
    }

    // tilt south
    val doSouthTilt = mutableListOf<String>()
    for (c in tiltedWest.first().indices) {
        val column = tiltedWest.map { line -> line[c] }.toTypedArray().toCharArray().joinToString("").reversed()

        doSouthTilt.add( column.split("#").map { it.toCharArray().sortedDescending().toCharArray().joinToString("") }.joinToString("#") )
    }

    val tiltedSouth = mutableListOf<String>()
    for (c in doSouthTilt.indices) {
        val row = doSouthTilt.map { line -> line.reversed()[c] }.toTypedArray().toCharArray().joinToString("")
        tiltedSouth.add(row)
    }

    // tilt east
    val tiltedEast = mutableListOf<String>()
    for (row in tiltedSouth) {
        tiltedEast.add( row.reversed().split("#").map { it.toCharArray().sortedDescending().toCharArray().joinToString("") }.joinToString("#").reversed() )
    }

    return tiltedEast
}