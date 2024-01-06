package day15

import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val input = Path("input/day15.txt").readText()

    val result = input.split(",").map { hash(it) }.sum()
    println(result)

    val instructions = input.split(",")

    val boxes = MutableList(256) { mutableMapOf<String, Int>() }

    instructions.forEach { instruction ->
        when {
            instruction.endsWith("-") -> {
                val label = instruction.dropLast(1)
                val box = hash(label)
                boxes[box].remove(label)
            }
            instruction.contains('=') -> {
                val (label, fl) = instruction.split("=")
                val box = hash(label)
                boxes[box][label] = fl.toInt()
            }
        }
    }

    val result2 = boxes.mapIndexed { box, lenses -> lenses.values.foldIndexed(0) { index, acc, next -> acc + ((index + 1) * next * (box + 1)) } }.sum()
    println(result2)
}

/*
Determine the ASCII code for the current character of the string.
Increase the current value by the ASCII code you just determined.
Set the current value to itself multiplied by 17.
Set the current value to the remainder of dividing itself by 256.
 */

fun hash(s: String) = s.toCharArray().fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
