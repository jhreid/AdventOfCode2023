package day11

import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

fun main() {
    val input = Path("input/day11.txt").readLines().map { line -> line.toCharArray().toList() }

    val galaxies = findGalaxies(input)

    val factor = 2L
    val universe = expand(input, galaxies, factor)

    //println(universe)

    val shortestPaths = shortestPathCalculation(universe)

    println(shortestPaths.sum())

    val bigFactor = 1000000L
    val bigUniverse = expand(input, galaxies, bigFactor)

    //println(bigUniverse)

    val bigShortestPaths = shortestPathCalculation(bigUniverse)

    println(bigShortestPaths.sum())
}

private fun shortestPathCalculation(universe: List<Pos>): List<Long> {
    val shortestPaths = mutableListOf<Long>()
    universe.forEachIndexed { index, galaxy ->
        universe.forEachIndexed { index2, galaxy2 ->
            if (index2 > index) {
                shortestPaths.add(shortestPath(galaxy, galaxy2))
            }
        }
    }
    return shortestPaths
}

data class Pos(val x: Long, val y: Long)

private fun shortestPath(first: Pos, second: Pos): Long {
    return abs(second.x - first.x) + abs(second.y - first.y)
}

private fun findGalaxies(universe: List<List<Char>>): List<Pos> {
    val galaxies = mutableListOf<Pos>()
    for (y in universe.indices) {
        for (x in universe[y].indices) {
            if (universe[y][x] == '#') {
                galaxies.add(Pos(x.toLong(), y.toLong()))
            }
        }
    }
    return galaxies
}

private fun expand(input: List<List<Char>>, galaxies: List<Pos>, factor: Long): List<Pos> {
    val emptyRows = emptyList<Int>().toMutableList()
    input.forEachIndexed { index, row ->
        if (!row.contains('#')) {
            emptyRows.add(index)
        }
    }

    val emptyColumns = emptyList<Int>().toMutableList()
    input[0].forEachIndexed loop@{ index, _ ->
        if (input.any { it[index] == '#' }) {
            return@loop
        }
        emptyColumns.add(index)
    }

    val expanded = galaxies.toMutableList()
    var delta = 0L
    emptyColumns.forEach { index ->
        expanded.forEachIndexed { g, galaxy ->
            if (galaxy.x > index + delta) {
                expanded[g] = Pos(galaxy.x + factor - 1, galaxy.y)
            }
        }
        delta += factor - 1
    }
    delta = 0
    emptyRows.forEach { index ->
        expanded.forEachIndexed { g, galaxy ->
            if (galaxy.y > index + delta) {
                expanded[g] = Pos(galaxy.x, galaxy.y + factor - 1)
            }
        }
        delta += factor - 1
    }

    //expanded.forEach { println(it.joinToString("")) }

    return expanded
}