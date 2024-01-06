package day02

import java.io.File

fun main() {
    val input = File("input/day02.txt").readLines()
    val maxRed = 12
    val maxGreen = 13
    val maxBlue = 14

    val colorCount = input.map { line -> line.split(":") }
        .map { line -> line[0] to line[1] }.toMap()
        .map { (key, value) -> key to maxColor(value) }

    val filtered = colorCount.filter { (_, colors) ->
        colors["red"]!! <= maxRed && colors["green"]!! <= maxGreen && colors["blue"]!! <= maxBlue
    }.toMap()
    val total = filtered.keys.fold(0) { acc, result -> acc + result.split(" ")[1].toInt() }
    println(total)

    val powers = colorCount.map { (key, colors) -> key to colors["red"]!! * colors["green"]!! * colors["blue"]!! }.toMap()
    val powerTotal = powers.values.fold(0) { acc, result -> acc + result }
    println(powerTotal)
}

fun maxColor(colorSet: String): Map<String, Int> {
    val colors = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
    val sets = colorSet.split(";")
    sets.forEach { set ->
        run {
            val groups = set.split(",")
            groups.forEach { group ->
                run {
                    val color = group.trim().split(" ")
                    if (colors[color[1]]!! < color[0].toInt()) {
                        colors[color[1]] = color[0].toInt()
                    }
                }
            }
        }
    }

    return colors.toMap()

}
