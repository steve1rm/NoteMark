import kotlinx.datetime.Clock
import me.androidbox.settings.presentation.utils.SettingsTimeFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

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
        // ITS HARDCODED AND I DONT LIKE IT PLEASE GIVE SOME FEEDBACK
        val timeInMillis = Clock.System.now().minus(30.days).toEpochMilliseconds()
        val formattedDate = SettingsTimeFormatter.formatDate(timeInMillis)
        assertEquals("20 JUN 2025, 16:49", formattedDate)
    }

}