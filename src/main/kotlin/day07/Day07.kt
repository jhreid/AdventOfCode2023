package day07

import java.io.File

fun main() {
    val input = File("input/day07.txt").readLines()
    val handTypes = mapOf(7 to "Five of a kind", 6 to "Four of a kind", 5 to "Full house", 4 to "Three of a kind", 3 to "Two pair", 2 to "One pair", 1 to "High card")

    val hands = input.map {
        val (hand, bid) = it.split(" ")
        hand to bid
    }.toMap().toSortedMap(handComparator)

    hands.entries.forEachIndexed { index, (hand, bid) ->
        val type = handType(hand)
        //println("$hand: ${handTypes[type.first]} ${index + 1} $bid ${bid.toInt() * (index + 1)}")
    }

    val result = hands.values.mapIndexed { index, bid -> bid.toInt() * (index + 1) }
    println("${result.sum()}")
}

val handComparator = Comparator<String> { hand, other ->
    val handType = handType(hand!!)
    val otherType = handType(other!!)

    if (handType.first != otherType.first) return@Comparator handType.first - otherType.first

    for (i in 0..<hand.length) {
        if (hand[i] != other[i]) {
            return@Comparator cardValue(hand[i]) - cardValue(other[i])
        }
    }
    0
}

fun handType(hand: String): Pair<Int, Map<Char, Int>> {
    val counts = cardCount(hand)

    val type = when {
        counts.containsValue(5) -> 7 // five of a kind
        counts.containsValue(4) -> 6 // four of a kind
        (counts.size == 2 && counts.containsValue(3) && counts.containsValue(2)) || (counts.size == 3 && counts.containsKey('J') && counts['J'] == 1 && counts.containsValue(3)) -> 5 // full house
        counts.containsValue(3) -> 4 // three of a kind
        counts.containsValue(2) && counts.values.size == 3 -> 3 // two pair
        counts.containsValue(2) -> 2 // one pair
        else -> 1
    }

    return Pair(type, counts)
}

fun cardCount(hand: String): Map<Char, Int> {
    val cardCounts = mutableMapOf<Char, Int>()

    hand.forEach {
        cardCounts.putIfAbsent(it, 0)
        cardCounts[it] = cardCounts[it]!! + 1
    }

    val numberOfJokers = cardCounts.getOrDefault('J', 0)
    cardCounts.forEach { card, total -> if (card != 'J') cardCounts[card] = total + numberOfJokers }

    return cardCounts
}

fun cardValue(card: Char): Int {
    return when (card) {
        'A' -> 14
        'K' -> 13
        'Q' -> 12
        'J' -> 1
        'T' -> 10
        else -> card.digitToInt()
    }
}