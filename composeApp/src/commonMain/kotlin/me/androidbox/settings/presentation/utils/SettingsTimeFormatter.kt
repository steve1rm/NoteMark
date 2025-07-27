@file:OptIn(ExperimentalTime::class)

package me.androidbox.settings.presentation.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

object SettingsTimeFormatter {
    fun formatDate(timeInMillis: Long?): String {
        val fiveMinutesInMillis = 5 * 60 * 1000
        val sevenDaysInMillis = 7 * 24 * 60 * 60 * 1000

        if (timeInMillis == null) {
            return "Never synced"
        }

        val instant = kotlin.time.Instant.fromEpochMilliseconds(timeInMillis)
        val currentTimeInstant = kotlin.time.Clock.System.now()

        val dateSubtractedInMillis = currentTimeInstant.minus(instant).inWholeMilliseconds

        return when {
            dateSubtractedInMillis <= fiveMinutesInMillis -> "Just now"
            dateSubtractedInMillis <= sevenDaysInMillis -> formatDateWeekly(timeInMillis)
            else -> formatDateFull(timeInMillis)
        }
    }

    private fun formatDateWeekly(timeInMillis: Long): String {
        val timeZone = TimeZone.UTC
        val instant = kotlin.time.Instant.fromEpochMilliseconds(timeInMillis)
        val currentInstant = kotlin.time.Clock.System.now()

        val duration = currentInstant - instant

        return when {
            duration < 1.hours -> "${duration.inWholeMinutes} minutes ago"
            duration < 1.days -> "${duration.inWholeHours} hours ago"
            duration < 7.days -> "${duration.inWholeDays} days ago"
            else -> instant.toLocalDateTime(timeZone).date.toString()
        }
    }

    private fun formatDateFull(timeInMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(timeInMillis)
        val date = instant.toLocalDateTime(TimeZone.UTC)

        val day = date.dayOfMonth
        val month = date.month.name.uppercase().take(3)
        val year = date.year
        val hour = date.hour.toString().padStart(2, '0')
        val minute = date.minute.toString().padStart(2, '0')

        return "$day $month $year, $hour:$minute"
    }
}