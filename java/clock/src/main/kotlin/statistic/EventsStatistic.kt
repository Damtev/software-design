package statistic

interface EventsStatistic {
    fun incEvent(name: String)

    fun getEventStatisticsByName(name: String): Double

    fun getAllEventStatistics(): Map<String, Double>

    fun printStatistic()
}