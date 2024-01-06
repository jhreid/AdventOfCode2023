package day16

import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val cave = Path("input/day16.txt").readLines().map { it.toCharArray() }.toTypedArray()

    val energized = fireBeam(cave, Pos(0, 0), 'R')

    println(energized.size)

    val part2 = mutableMapOf<Pair<Pos, Char>, Int>()

    for (x in cave[0].indices) {
        part2[Pair(Pos(x, 0), 'D')] = fireBeam(cave, Pos(x, 0), 'D').size
        part2[Pair(Pos(x, cave.size - 1), 'U')] = fireBeam(cave, Pos(x, cave.size - 1), 'U').size
    }
    for (y in cave.indices) {
        part2[Pair(Pos(0, y), 'R')] = fireBeam(cave, Pos(0, y), 'R').size
        part2[Pair(Pos(cave[0].size - 1, y), 'L')] = fireBeam(cave, Pos(cave[0].size - 1, y), 'L').size
    }

    println(part2.values.max())
}

fun fireBeam(cave: Array<CharArray>, start: Pos, direction: Char): Set<Pos> {
    val queue = mutableListOf(Pair(start, direction))
    val visited = mutableSetOf<Pair<Pos, Char>>()

    while (queue.isNotEmpty()) {
        val j = queue.size
        for (i in 0..<j) {
            if (visited.contains(queue[0])) {
                queue.removeAt(0)
                continue
            } else {
                visited.add(queue[0])
            }
            val currentPosition = queue[0].first
            if (currentPosition.x < 0 || currentPosition.x >= cave[0].size || currentPosition.y < 0 || currentPosition.y >= cave.size) {
                queue.removeAt(0)
                continue
            }
            val currentDirection = queue[0].second
            val currentSpace = cave[currentPosition.y][currentPosition.x]
            queue.removeAt(0)

            val neighbours = mapOf(
                'R' to Pair(Pos(currentPosition.x + 1, currentPosition.y), 'R'),
                'L' to Pair(Pos(currentPosition.x - 1, currentPosition.y), 'L'),
                'U' to Pair(Pos(currentPosition.x, currentPosition.y - 1), 'U'),
                'D' to Pair(Pos(currentPosition.x, currentPosition.y + 1), 'D')
            )

            when (currentSpace) {
                '|' -> when (currentDirection) {
                    'R', 'L' -> {
                        queue.add(neighbours['U']!!)
                        queue.add(neighbours['D']!!)
                    }
                    'U' -> {
                        queue.add(neighbours['U']!!)
                    }
                    else -> {
                        queue.add(neighbours['D']!!)
                    }
                }
                '-' -> when (currentDirection) {
                    'U', 'D' -> {
                        queue.add(neighbours['R']!!)
                        queue.add(neighbours['L']!!)
                    }
                    'R' -> {
                        queue.add(neighbours['R']!!)
                    }
                    else -> {
                        queue.add(neighbours['L']!!)
                    }
                }
                '\\' -> when (currentDirection) {
                    'U' -> queue.add(neighbours['L']!!)
                    'D' -> queue.add(neighbours['R']!!)
                    'L' -> queue.add(neighbours['U']!!)
                    'R' -> queue.add(neighbours['D']!!)
                }
                '/' -> when (currentDirection) {
                    'U' -> queue.add(neighbours['R']!!)
                    'D' -> queue.add(neighbours['L']!!)
                    'L' -> queue.add(neighbours['D']!!)
                    'R' -> queue.add(neighbours['U']!!)
                }
                '.' -> queue.add(neighbours[currentDirection]!!)
            }

        }
    }

    return visited.map { it.first }.filter { it.x >= 0 && it.y >= 0 && it.x < cave[0].size && it.y < cave.size }.toSet()
}

data class Pos(val x: Int, val y: Int)

