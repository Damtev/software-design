package statistic

import clock.Clock
import kotlin.math.max

@ExperimentalUnsignedTypes
class ClockEventsStatistic(private val clock: Clock) : EventsStatistic {
    private val events = mutableMapOf<String, MutableList<ULong>>()

    companion object {
        private const val MINUTES_IN_HOUR = 60UL
        private const val SECONDS_IN_HOUR = 3600UL
    }

    override fun incEvent(name: String) {
        events.getOrPut(name, { mutableListOf() }).add(clock.now().epochSecond.toULong())
    }

    override fun getEventStatisticsByName(name: String): Double {
        val eventsByName = events.getOrDefault(name, mutableListOf())
        val now = clock.now().epochSecond
        val hourAgo = max(now - SECONDS_IN_HOUR.toLong(), 0).toULong()
        val countLastHour = eventsByName.filter { time -> time in hourAgo..now.toULong() }.count()
        return countLastHour.toDouble() / MINUTES_IN_HOUR.toDouble()
    }

    override fun getAllEventStatistics(): Map<String, Double> = events.mapValues { event ->
        getEventStatisticsByName(event.key)
    }

    override fun printStatistic() = events.forEach { event ->
        println("Event: ${event.key}, rpm: ${event.value.size.toDouble() / MINUTES_IN_HOUR.toDouble()}")
    }
}