package day03

import java.io.File

fun main() {
    val input = File("input/day03.txt").readLines()

    val parts = mutableListOf<String>()

    for (row in input.indices) {
        var col = 0
        var currentNumber = ""
        var isPart = false
        while (col < input[0].length) {
            if (input[row][col].isDigit()) {
                currentNumber += input[row][col].toString()
                if (col > 0 && !input[row][col - 1].isDigit() && input[row][col - 1] != '.') {
                    isPart = true
                }
                if (col < input[0].length - 1 && !input[row][col + 1].isDigit() && input[row][col + 1] != '.') {
                    isPart = true
                }
                if (row > 0 && !input[row - 1][col].isDigit() && input[row - 1][col] != '.') {
                    isPart = true
                }
                if (row < input.size - 1 && !input[row + 1][col].isDigit() && input[row + 1][col] != '.') {
                    isPart = true
                }
                if (col > 0 && row > 0 && !input[row - 1][col - 1].isDigit() && input[row - 1][col - 1] != '.') {
                    isPart = true
                }
                if (col < input[0].length - 1 && row < input.size - 1 && !input[row + 1][col + 1].isDigit() && input[row + 1][col + 1] != '.') {
                    isPart = true
                }
                if (col < input[0].length - 1 && row > 0 && !input[row - 1][col + 1].isDigit() && input[row - 1][col + 1] != '.') {
                    isPart = true
                }
                if (col > 0 && row  < input.size - 1 && !input[row + 1][col - 1].isDigit() && input[row + 1][col - 1] != '.') {
                    isPart = true
                }
            } else {
                if (isPart) {
                    parts.add(currentNumber)
                }
                currentNumber = ""
                isPart = false
            }
            col++
            if (col == input[0].length && isPart) {
                parts.add(currentNumber)
                currentNumber = ""
                isPart = false
            }
        }
    }

    val total = parts.fold(0){ acc, next -> acc + next.toInt()}
    println(total)

    val ratios = findGearRatios(input)
    println(ratios)
}

fun findGearRatios(input: List<String>): Int {
    var ratios = mutableListOf<MutableMap<String, String>>()

    for (row in input.indices) {
        var col = 0
        while (col < input[0].length) {
            if (input[row][col] == '*') {
                val partNumbers = mutableMapOf("topleft" to "", "top" to "", "topright" to "", "left" to "", "right" to "", "bottomleft" to "", "bottom" to "", "bottomright" to "")

                if (row > 0) {
                    if (input[row - 1][col] == '.') {
                        // check diagonals
                        // top left
                        if (col > 0 && input[row - 1][col - 1].isDigit()) {
                            var number = ""
                            for (i in col - 1 downTo 0) {
                                if (input[row - 1][i] == '.') {
                                    break
                                }
                                number = input[row - 1][i].toString() + number
                            }
                            partNumbers["topleft"] = number
                        }
                        // top right
                        if (col < input[0].length - 1 && input[row - 1][col + 1].isDigit()) {
                            var number = ""
                            for (i in col + 1..<input[0].length) {
                                if (input[row - 1][i] == '.') {
                                    break
                                }
                                number += input[row - 1][i].toString()
                            }
                            partNumbers["topright"] = number
                        }
                    } else if (input[row - 1][col].isDigit()) {
                        // check above
                        partNumbers["top"] = input[row - 1][col].toString()
                        // walk back
                        if (col > 0) {
                            for (i in col - 1 downTo 0) {
                                if (input[row - 1][i] == '.') {
                                    break
                                }
                                partNumbers["top"] = input[row - 1][i].toString() + partNumbers["top"]
                            }
                        }
                        // walk forward
                        if (col < input[0].length - 1) {
                            for (i in col + 1..<input[0].length) {
                                if (input[row - 1][i] == '.') {
                                    break
                                }
                                partNumbers["top"] += input[row - 1][i].toString()
                            }
                        }
                    }
                }

                if (row < input.size - 1) {
                    if (input[row + 1][col] == '.') {
                        // check diagonals
                        // bottom left
                        if (col > 0 && input[row + 1][col - 1].isDigit()) {
                            var number = ""
                            for (i in col - 1 downTo 0) {
                                if (input[row + 1][i] == '.') {
                                    break
                                }
                                number = input[row + 1][i].toString() + number
                            }
                            partNumbers["bottomleft"] = number
                        }
                        // bottom right
                        if (col < input[0].length - 1 && input[row + 1][col + 1].isDigit()) {
                            var number = ""
                            for (i in col + 1..<input[0].length) {
                                if (input[row + 1][i] == '.') {
                                    break
                                }
                                number += input[row + 1][i].toString()
                            }
                            partNumbers["bottomright"] = number
                        }
                    } else if (input[row + 1][col].isDigit()) {
                        // check below
                        partNumbers["bottom"] = input[row + 1][col].toString()
                        // walk back
                        if (col > 0) {
                            for (i in col - 1 downTo 0) {
                                if (input[row + 1][i] == '.') {
                                    break
                                }
                                partNumbers["bottom"] = input[row + 1][i].toString() + partNumbers["bottom"]
                            }
                        }
                        // walk forward
                        if (col < input[0].length - 1) {
                            for (i in col + 1..<input[0].length) {
                                if (input[row + 1][i] == '.') {
                                    break
                                }
                                partNumbers["bottom"] += input[row + 1][i].toString()
                            }
                        }
                    }
                }

                if (col > 0) {
                    // check left
                    for (i in col - 1 downTo 0) {
                        if (input[row][i] == '.') {
                            break
                        }
                        partNumbers["left"] = input[row][i].toString() + partNumbers["left"]
                    }
                }

                if (col < input[0].length - 1) {
                    // walk forward
                    for (i in col + 1..<input[0].length) {
                        if (input[row][i] == '.') {
                            break
                        }
                        partNumbers["right"] += input[row][i].toString()
                    }
                }

                if (partNumbers.filterValues { it.isNotEmpty() }.size == 2) {
                    ratios.add(partNumbers)
                }
            }
            col++
        }
    }

    return ratios.fold(0){ acc, next -> acc + next.filterValues { it.isNotEmpty() }.values.fold(1){ acc1, next1 -> acc1 * next1.toInt()} }
}
