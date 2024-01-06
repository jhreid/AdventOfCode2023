package day21

import kotlin.io.path.Path
import kotlin.io.path.readLines

data class Pos(val x: Int, val y: Int)

fun main() {
    val farm = Path("input/day21test.txt").readLines().map { it.toCharArray() }.toTypedArray()

    val start = findStart(farm)

    val result = takeSteps(farm, start, 64)

    println(result)
}

fun takeSteps(farm: Array<CharArray>, start: Pos, count: Long): Int {
    val visited = mutableSetOf(Pair(start, 0L))
    for (step in 1..count) {
        val next = mutableSetOf<Pair<Pos, Long>>()
        visited.filter { it.second == step - 1 }.forEach{ (position, _) ->
            val north = Pos(position.x, position.y - 1)
            val south = Pos(position.x, position.y + 1)
            val east = Pos(position.x + 1, position.y)
            val west = Pos(position.x - 1, position.y)
            if (!visited.contains(Pair(north, step)) && north.y > -1 && farm[north.y][north.x] != '#') {
                next.add(Pair(north, step))
            }
            if (!visited.contains(Pair(south, step)) && south.y < farm.size && farm[south.y][south.x] != '#') {
                next.add(Pair(south, step))
            }
            if (!visited.contains(Pair(east, step)) && east.y > -1 && farm[east.y][east.x] != '#') {
                next.add(Pair(east, step))
            }
            if (!visited.contains(Pair(west, step)) && west.y < farm[0].size && farm[west.y][west.x] != '#') {
                next.add(Pair(west, step))
            }
        }
        visited.addAll(next)
    }

    return visited.filter { it.second == count }.size
}

fun findStart(farm: Array<CharArray>): Pos {
    for (y in farm.indices) {
        val x = farm[y].indexOf('S')
        if (x != -1) {
            return Pos(x, y)
        }
    }
    return(Pos(-1, -1))
}