package day18

import kotlin.io.path.Path
import kotlin.io.path.readLines

data class Pos(val x: Int, val y: Int)

fun main() {
    val digPlan = Path("input/day18.txt").readLines().map { it.split(" ") }.toList()

    val trench = digTrench(digPlan)
    val hole = digHole(trench)

    printHole(hole)
    println(hole.size)
}

fun printHole(hole: Set<Pos>) {
    val miny = hole.map { it.y }.min()
    val maxy = hole.map { it.y }.max()
    val minx = hole.map { it.x }.min()
    val maxx = hole.map { it.x }.max()
    for (y in miny..maxy) {
        for (x in minx..maxx) {
            if (hole.contains(Pos(x, y))) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }
}

fun digTrench(digPlan: List<List<String>>): Set<Pos> {
    val trench = mutableListOf<Pos>()

    var currentHole = Pos(0, 0)
    digPlan.forEach { instruction ->
        val distance = instruction[1].toInt()
        when (instruction[0]) {
            "R" -> {
                for (x in currentHole.x..(currentHole.x + distance)) {
                    currentHole = Pos(x, currentHole.y)
                    trench.add(currentHole)
                }
            }
            "L" -> {
                for (x in currentHole.x downTo  (currentHole.x - distance)) {
                    currentHole = Pos(x, currentHole.y)
                    trench.add(currentHole)
                }
            }
            "U" -> {
                for (y in currentHole.y downTo  (currentHole.y - distance)) {
                    currentHole = Pos(currentHole.x, y)
                    trench.add(currentHole)
                }
            }
            "D" -> {
                for (y in currentHole.y..(currentHole.y + distance)) {
                    currentHole = Pos(currentHole.x, y)
                    trench.add(currentHole)
                }
            }
        }
    }

    return trench.toSet()
}

fun digHole(trench: Set<Pos>): Set<Pos> {
    val hole = trench.toMutableList()
    val starty = trench.map { it.y }.min() + 1
    val startx = trench.filter { it.y == starty }.map { it.x }.min() + 1
    val startPos = Pos(startx, starty)

    val queue = mutableListOf(startPos)

    while (queue.isNotEmpty()) {
        val currentPos = queue.first()
        queue.removeAt(0)

        val left = Pos(currentPos.x - 1, currentPos.y)
        if (!trench.contains(left) && !hole.contains(left) && !queue.contains(left)) {
            queue.add(left)
        }
        val right = Pos(currentPos.x + 1, currentPos.y)
        if (!trench.contains(right) && !hole.contains(right) && !queue.contains(right)) {
            queue.add(right)
        }
        val up = Pos(currentPos.x, currentPos.y - 1)
        if (!trench.contains(up) && !hole.contains(up) && !queue.contains(up)) {
            queue.add(up)
        }
        val down = Pos(currentPos.x, currentPos.y + 1)
        if (!trench.contains(down) && !hole.contains(down) && !queue.contains(down)) {
            queue.add(down)
        }

        hole.add(currentPos)
    }

    return hole.toSet()
}
