package day10

import kotlin.io.path.Path
import kotlin.io.path.readLines

/*
| is a vertical pipe connecting north and south.
- is a horizontal pipe connecting east and west.
L is a 90-degree bend connecting north and east.
J is a 90-degree bend connecting north and west.
7 is a 90-degree bend connecting south and west.
F is a 90-degree bend connecting south and east.
. is ground; there is no pipe in this tile.
S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.
*/

fun main() {
    val input = Path("input/day10.txt").readLines()

    val maze = input.map { it.toCharArray() }.toTypedArray()

    var start = Pos(0, 0)
    for (y in maze.indices) {
        if (maze[y].contains('S')) {
            val x = maze[y].indexOf('S')
            start = Pos(x, y)
            break
        }
    }

    val pointsOnPath = walkPipes(maze, start)
    println(pointsOnPath.values.max())

    // Part 2 - count points inside
    substituteStart(start, maze)
    val pointsInside = calculatePointsInside(maze, pointsOnPath.keys)
    println(pointsInside)
}

fun substituteStart(start: Pos, maze: Array<CharArray>) {
    val (x, y) = start
    if (y + 1 < maze.size && y - 1 >= 0) {
        if ("|F7".contains(maze[y - 1][x]) && "|LJ".contains(maze[y + 1][x])) {
            maze[y][x] = '|'
        }
    }
    if (x + 1 < maze[y].size && x - 1 >= 0) {
        if ("-FL".contains(maze[y][x - 1]) && "-7J".contains(maze[y][x + 1])) {
            maze[y][x] = '-'
        }
    }
    if (y - 1 >= 0) {
        if ("|F7".contains(maze[y - 1][x])) {
            if (x - 1 >= 0) {
                if ("FL-".contains(maze[y][x - 1])) {
                    maze[y][x] = 'J'
                }
            } else if (x + 1 < maze[y].size) {
                if ("J7-".contains(maze[start.y][start.x + 1])) {
                    maze[y][x] = 'L'
                }
            }
        }
    } else if (y + 1 < maze[y].size) {
        if ("|JL".contains(maze[y + 1][x])) {
            if (x - 1 >= 0) {
                if ("FL-".contains(maze[y][x - 1])) {
                    maze[y][x] = '7'
                }
            } else if (x + 1 < maze[y].size) {
                if ("J7-".contains(maze[y][x + 1])) {
                    maze[y][x] = 'F'
                }
            }
        }
    }
}

data class Pos(val x: Int, val y: Int)

fun walkPipes(maze: Array<CharArray>, start: Pos): Map<Pos, Int> {
    val queue = mutableListOf(start)
    val visited = mutableMapOf<Pos, Int>()
    visited[start] = 0

    var pathLength = 1
    while (queue.size > 0) {
        val j = queue.size
        for (i in 0..<j) {
            val current = queue[0]
            val currentShape = maze[current.y][current.x]
            queue.removeAt(0)

            val neighbours = mapOf(
                'E' to Pos(current.x + 1, current.y),
                'W' to Pos(current.x - 1, current.y),
                'N' to Pos(current.x, current.y - 1),
                'S' to Pos(current.x, current.y + 1)
            )

            neighbours.forEach loop@{ d, n ->
                if (n.x < 0 || n.x >= maze[0].size || n.y < 0 || n.y >= maze.size) {
                    return@loop
                }
                if (visited.containsKey(n)) {
                    return@loop
                }
                val neighbour = maze[n.y][n.x]
                if (neighbour == '.') {
                    return@loop
                }
                when (currentShape) {
                    '|' -> if (d == 'E' || d == 'W') return@loop // connects N and S - ignore E and W
                    '-' -> if (d == 'N' || d == 'S') return@loop // connects E and W
                    'L' -> if (d == 'W' || d == 'S') return@loop // connects N and E
                    'J' -> if (d == 'E' || d == 'S') return@loop // connects N and W
                    '7' -> if (d == 'E' || d == 'N') return@loop // connects W and S
                    'F' -> if (d == 'W' || d == 'N') return@loop // connects E and S
                    'S' -> if ((d == 'W' && "|J7.".contains(neighbour)) || (d == 'E' && "|FL.".contains(neighbour)) || (d == 'N' && "-JL.".contains(
                            neighbour
                        )) || (d == 'S' && "-F7.".contains(neighbour))
                    ) return@loop // neighbour is a special case as it can be anything
                }

                //println(maze[n.y][n.x])
                visited[n] = pathLength
                queue.add(n)
            }
        }

        pathLength++
    }

    return visited.toMap()
}

fun calculatePointsInside(maze: Array<CharArray>, pointsOnPath: Set<Pos>): Int {
    val pointsInside = mutableSetOf<Pos>()
    val pointsOutside = mutableSetOf<Pos>()
    maze.indices.forEach { y ->
        maze[y].indices.forEach loop@{ x ->
            val pos = Pos(x, y)
            if (pointsOnPath.contains(pos)) {
                return@loop
            }
            if (isInside(maze, pos, pointsOnPath)) {
                pointsInside.add(pos)
            } else {
                pointsOutside.add(pos)
            }
        }
    }

    processAscii(maze, pointsOnPath, pointsInside)

    return pointsInside.size
}

fun processAscii(maze: Array<CharArray>, pointsOnPath: Set<Pos>, pointsInside: Set<Pos>) {
    for (y in maze.indices) {
        for (x in maze[y].indices) {
            if (pointsOnPath.contains(Pos(x, y))) {
                when (maze[y][x]) {
                    'J' -> maze[y][x] = '╝'
                    '7' -> maze[y][x] = '╗'
                    'F' -> maze[y][x] = '╔'
                    'L' -> maze[y][x] = '╚'
                    '|' -> maze[y][x] = '║'
                    '-' -> maze[y][x] = '═'
                }
            } else if (pointsInside.contains(Pos(x, y))) {
                maze[y][x] = '▒'
            } else {
                maze[y][x] = ' '
            }
        }
        println(maze[y].joinToString(""))
    }
}

fun isInside(maze: Array<CharArray>, pos: Pos, pointsOnPath: Set<Pos>): Boolean {
    val left = checkLeft(maze, pos, pointsOnPath)
    return left
}

fun checkLeft(maze: Array<CharArray>, pos: Pos, pointsOnPath: Set<Pos>): Boolean {
    var crossedLine = 0
    var metCorner = '0'
    for (x in pos.x downTo 0) {
        val test = Pos(x, pos.y)
        if (pointsOnPath.contains(test)) {
            val piece = maze[pos.y][x]
            when {
                piece == '|' -> {
                    crossedLine++
                }

                'F' == piece -> {
                    if ('7' == metCorner) {
                        metCorner = '0'
                    } else if ('J' == metCorner) {
                        metCorner = '0'
                        crossedLine++
                    } else {
                        metCorner = piece
                    }
                }

                'L' == piece -> {
                    if ('J' == metCorner) {
                        metCorner = '0'
                    } else if ('7' == metCorner){
                        metCorner = '0'
                        crossedLine++
                    } else {
                        metCorner = piece
                    }
                }

                "7J".contains(piece) -> {
                    metCorner = piece
                }
            }
        }
    }
    return crossedLine % 2 == 1
}

