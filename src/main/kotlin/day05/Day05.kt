package day05

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File

suspend fun main() {
    val sections = File("input/day05.txt").readText().split(System.lineSeparator() + System.lineSeparator())
    val seeds = sections[0].split(" ").drop(1)
    val seedToSoilMap = processMap(sections[1])
    val soilToFertilizerMap = processMap(sections[2])
    val fertilizerToWaterMap = processMap(sections[3])
    val waterToLightMap = processMap(sections[4])
    val lightToTemperatureMap = processMap(sections[5])
    val temperatureToHumidityMap = processMap(sections[6])
    val humidityToLocationMap = processMap(sections[7]).toSortedMap(compareBy { it.first })

    var seedLocation = Pair<Long, Long>(-1, Long.MAX_VALUE)
    val results = mutableListOf<Pair<Long, Long>>()

    coroutineScope {
        var routines = mutableListOf<Deferred<Pair<Long, Long>>>()
        seeds.chunked(2).forEach {
            val (s, n) = it
            val routine = async {
                processSeed(
                    Pair(s, n),
                    seedToSoilMap,
                    soilToFertilizerMap,
                    fertilizerToWaterMap,
                    waterToLightMap,
                    lightToTemperatureMap,
                    temperatureToHumidityMap,
                    humidityToLocationMap
                )
            }
            routines.add(routine)
        }

        routines.forEach{
            val result = it.await()
            results.add(result)
        }

        results.forEach {
            if (it.second < seedLocation.second) {
                seedLocation = it
            }
        }

    }
    println(seedLocation)
}

fun processSeed(seedRange: Pair<String, String>,
                seedToSoilMap: Map<Pair<Long, Long>, Long>,
                soilToFertilizerMap: Map<Pair<Long, Long>, Long>,
                fertilizerToWaterMap: Map<Pair<Long, Long>, Long>,
                waterToLightMap: Map<Pair<Long, Long>, Long>,
                lightToTemperatureMap: Map<Pair<Long, Long>, Long>,
                temperatureToHumidityMap: Map<Pair<Long, Long>, Long>,
                humidityToLocationMap: Map<Pair<Long, Long>, Long>): Pair<Long, Long> {
    val (s, n) = seedRange
    val seedStart = s.toLong()
    val range = n.toLong()
    var seedLocation = Pair<Long, Long>(-1, Long.MAX_VALUE)
    for (seed in seedStart..<seedStart + range) {
        val soilMap = seedToSoilMap.filterKeys { seed >= it.first && seed <= it.second }
        val soil = if (soilMap.isEmpty()) {
            seed
        } else {
            seed + soilMap.values.first()
        }

        val fertilizerMap = soilToFertilizerMap.filterKeys { soil >= it.first && soil <= it.second }
        val fertilizer = if (fertilizerMap.isEmpty()) {
            soil
        } else {
            soil + fertilizerMap.values.first()
        }

        val waterMap = fertilizerToWaterMap.filterKeys { fertilizer >= it.first && fertilizer <= it.second }
        val water = if (waterMap.isEmpty()) {
            fertilizer
        } else {
            fertilizer + waterMap.values.first()
        }

        val lightMap = waterToLightMap.filterKeys { water >= it.first && water <= it.second }
        val light = if (lightMap.isEmpty()) {
            water
        } else {
            water + lightMap.values.first()
        }

        val temperatureMap = lightToTemperatureMap.filterKeys { light >= it.first && light <= it.second }
        val temperature = if (temperatureMap.isEmpty()) {
            light
        } else {
            light + temperatureMap.values.first()
        }

        val humidityMap =
            temperatureToHumidityMap.filterKeys { temperature >= it.first && temperature <= it.second }
        val humidity = if (humidityMap.isEmpty()) {
            temperature
        } else {
            temperature + humidityMap.values.first()
        }

        val locationMap = humidityToLocationMap.filterKeys { humidity >= it.first && humidity <= it.second }
        val location = if (locationMap.isEmpty()) {
            humidity
        } else {
            humidity + locationMap.values.first()
        }
        if (location < seedLocation.second ) {
            seedLocation = Pair(seed, location)
            println("Updated seed location: $seedLocation")
        }
    }

    return seedLocation
}

fun processMap(input: String): Map<Pair<Long, Long>, Long> {
    val output = mutableMapOf<Pair<Long, Long>, Long>()

    input.split(System.lineSeparator())
        .filter { it[0].isDigit() }
        .forEach { line ->
            run {
                val (destinationStart, sourceStart, rangeLength) = line.split("""\s+""".toRegex())
                output[Pair(sourceStart.toLong(), sourceStart.toLong() + rangeLength.toLong() - 1)] =
                    destinationStart.toLong() - sourceStart.toLong()
            }
        }
    return output.toMap()
}
