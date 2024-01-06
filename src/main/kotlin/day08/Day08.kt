package day08

import java.io.File

fun main() {
    val input = File("input/day08.txt").readLines()

    val directions = input[0].toCharArray()
    val nodesMap = input.slice(2..<input.size).map {
        val (start, rest) = it.split(" = ")
        val (left, right) = rest.substring(1, rest.length - 1).split(", ")
        start to Pair(left, right)
    }.toMap()

    println(nodesMap)
    val nodes = nodesMap.keys.filter { it.endsWith('A') }.toMutableList()
    val counts = mutableListOf<Long>()
    nodes.forEach {
        var node = it
        var count = 0L
        while (!node.endsWith("Z")) {
            val direction = directions[(count % directions.size).toInt()]
            if (direction == 'L') {
                node = nodesMap[node]!!.first
            } else {
                node = nodesMap[node]!!.second
            }
            count++
            if (node.endsWith('Z')) {
                counts.add(count)
                break
            }
        }
    }

    println(counts)
    println(lcm(counts))
}

fun lcm(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}