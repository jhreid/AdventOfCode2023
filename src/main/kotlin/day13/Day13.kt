package day13

import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val patterns = Path("input/day13.txt").readText().split("\n\n")

    val result = patterns.fold(0){ acc, next -> acc + findHorizontalReflection(next) + findVerticalReflection(next) }
    println(result)
}

fun findVerticalReflection(pattern: String): Int {
    val rows = pattern.split("\n")

    for (i in 1..<rows.size) {
        val a = rows.slice(0..<i)
        val b = rows.slice(i..<rows.size)

        if (a.size > b.size) {
            val first = a.reversed().slice(b.indices)
            val second = b

            if (first == second) {
                println("Reflection at $i")
                return i * 100
            }
        } else {
            val first = b.slice(a.indices)
            val second = a.reversed()

            if (first == second) {
                println("Reflection at $i")
                return i * 100
            }
        }
    }
    return 0
}

fun findHorizontalReflection(pattern: String): Int {
    val lines = pattern.split("\n")
    val columns = mutableListOf<String>()
    for (i in lines[0].indices) {
        columns.add(lines.map { it[i] }.joinToString(""))
    }

    for (i in 1..<columns.size) {
        val a = columns.slice(0..<i)
        val b = columns.slice(i..<columns.size)

        if (a.size > b.size) {
            val first = a.reversed().slice(b.indices)
            val second = b

            if (first == second) {
                println("Reflection at $i")
                return i
            }
        } else {
            val first = b.slice(a.indices)
            val second = a.reversed()

            if (first == second) {
                println("Reflection at $i")
                return i
            }
        }
    }
    return 0
}