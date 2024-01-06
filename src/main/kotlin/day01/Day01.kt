package day01

import java.io.File

fun main() {
    var total = 0
    File("input/day01.txt").forEachLine { line ->
        val input = replaceWord(line)
        val firstDigit = input.indexOfFirst { c -> c.isDigit() }
        val lastDigit = input.reversed().indexOfFirst { c -> c.isDigit() }
        val number = (input[firstDigit].toString() + input.reversed()[lastDigit].toString()).toInt()
        total += number
    }
    println(total)
}

fun replaceWord(line: String): String {
    val words = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
    val firstIndex = words.keys.map { it to line.indexOf(it) }.filter { it.second >= 0 }.sortedBy { it.second }.toMap()
    val lastIndex = words.keys.map { it to line.lastIndexOf(it) }.filter { it.second >= 0 }.sortedBy { it.second }.toMap()
    if (firstIndex.isNotEmpty()) {
        val first = firstIndex.keys.elementAt(0)
        val last = lastIndex.keys.last()
        return line
            .replace(first, words[first].toString())
            .replace(last, words[last].toString())
    } else {
        return line
    }
}
