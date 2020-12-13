import clock.SettableClock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statistic.ClockEventsStatistic
import statistic.EventsStatistic
import java.time.Instant

@ExperimentalUnsignedTypes
class ClockEventsStatisticTest {

    lateinit var clock: SettableClock
    lateinit var stats: EventsStatistic

    @BeforeEach
    fun init() {
        clock = SettableClock(Instant.ofEpochSecond(0))
        stats = ClockEventsStatistic(clock)
    }

    @Test
    fun testEmptyEvents() {
        assertEquals(0, stats.getAllEventStatistics().size)
    }

    @Test
    fun testOneEventOneInc() {
        val eventName = "event"
        stats.incEvent(eventName)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)
    }

    @Test
    fun testNonExistingEvent() {
        val eventName = "event"
        stats.incEvent(eventName)
        val nonExistingEvent = "non existing event"
        assertEquals(0.0, stats.getEventStatisticsByName(nonExistingEvent))
        assertEquals(1, stats.getAllEventStatistics().size)
    }

    @Test
    fun testOneEventManyIncs() {
        val eventName = "event"
        for (i in 1..1e3.toInt()) {
            stats.incEvent(eventName)
            assertEquals(i / 60.0, stats.getEventStatisticsByName(eventName))
            assertEquals(1, stats.getAllEventStatistics().size)
        }
    }

    @Test
    fun testOneEventOneIncSmallChangingTime() {
        val eventName = "event"

        stats.incEvent(eventName)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)

        clock.now = Instant.ofEpochSecond(100)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)
    }

    @Test
    fun testOneEventOneIncOneHourChangingTime() {
        val eventName = "event"

        stats.incEvent(eventName)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)

        clock.now = Instant.ofEpochSecond(3600)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)
    }

    @Test
    fun testOneEventOneIncMoreOneHourChangingTime() {
        val eventName = "event"

        stats.incEvent(eventName)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)

        clock.now = Instant.ofEpochSecond(3601)
        assertEquals(0.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)
    }

    @Test
    fun testOneEventManyIncsWithinHour() {
        val eventName = "event"

        stats.incEvent(eventName)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)

        clock.now = Instant.ofEpochSecond(3600)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)

        stats.incEvent(eventName)
        assertEquals(2.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)

        clock.now = Instant.ofEpochSecond(3601)
        assertEquals(1.0 / 60.0, stats.getEventStatisticsByName(eventName))
        assertEquals(1, stats.getAllEventStatistics().size)
    }

    @Test
    fun testManyEventsManyIncs() {
        val events = listOf(1, 2, 3, 4).map { it.toString() }
        val delays = listOf(7L, 13L, 17L, 19L)
        val eventsWithDelays = events.zip(delays)

        val count = 100
        for (i in 1..count) {
            for (eventWithDelay in eventsWithDelays) {
                clock.now = Instant.ofEpochSecond(i * eventWithDelay.second)
                stats.incEvent(eventWithDelay.first)
            }
        }
        assertEquals(events.size, stats.getAllEventStatistics().size)

        clock.now = Instant.ofEpochSecond(0L)
        for (eventName in events) {
            assertEquals(0.0, stats.getEventStatisticsByName(eventName))
        }

        val max = count * delays.last()
        for (i in 0 until max) {
            clock.now = Instant.ofEpochSecond(i)
            for (eventWithDelay in eventsWithDelays) {
                val delay = eventWithDelay.second
                if (i < count * delay) {
                    var first = delay * ((i - 3600) / delay)
                    if ((i - 3600) % delay != 0L) {
                        first += delay
                    }
                    val start = delay.coerceAtLeast(first)
                    val cnt = (i - start + delay).coerceAtLeast(0) / delay
                    assertEquals(cnt / 60.0, stats.getEventStatisticsByName(eventWithDelay.first))
                }
            }
        }
    }
}