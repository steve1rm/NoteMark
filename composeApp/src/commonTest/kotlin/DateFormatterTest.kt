import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import me.androidbox.settings.presentation.utils.SettingsTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class DateFormatterTest {

    @Test
    fun `Testing Setting's Time formatter (3 minutes ago)`() {
        val timeInMillis = Clock.System.now().minus(3.minutes).toEpochMilliseconds()
        val formattedDate = SettingsTimeFormatter.formatDate(timeInMillis)
        assertEquals("Just now", formattedDate)
    }

    @Test
    fun `Testing Setting's Time formatter (10 minutes ago)`() {
        val timeInMillis = Clock.System.now().minus(10.minutes).toEpochMilliseconds()
        val formattedDate = SettingsTimeFormatter.formatDate(timeInMillis)
        assertEquals("10 minutes ago", formattedDate)
    }


    @Test
    fun `Testing Setting's Time formatter (2 hours ago)`() {
        val timeInMillis = Clock.System.now().minus(2.hours).toEpochMilliseconds()
        val formattedDate = SettingsTimeFormatter.formatDate(timeInMillis)
        assertEquals("2 hours ago", formattedDate)
    }

    @Test
    fun `Testing Setting's Time formatter (4 days ago)`() {
        val timeInMillis = Clock.System.now().minus(4.days).toEpochMilliseconds()
        val formattedDate = SettingsTimeFormatter.formatDate(timeInMillis)
        assertEquals("4 days ago", formattedDate)
    }


    @Test
    fun `Testing Setting's Time formatter (1 month ago)`() {
        val timeInMillis = Instant
            .fromEpochMilliseconds(CURR_TIME_IN_MILLIS)
            .toEpochMilliseconds()

        val formattedDate = SettingsTimeFormatter.formatDate(timeInMillis)
        assertEquals("20 JUN 2025, 19:40", formattedDate)
    }

    @Test
    fun `Testing Setting's Time formatter (1 week and second)`() {
        val timeInMillis = Clock.System.now().minus(1.seconds).toEpochMilliseconds()

        val formattedDate = SettingsTimeFormatter.formatDate(timeInMillis)
        assertEquals("6 days", formattedDate)
    }

    companion object {
        const val CURR_TIME_IN_MILLIS = 1750448400000
    }

}