@file:OptIn(ExperimentalTime::class)

package me.androidbox.notes.presentation.note_details.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

object NoteDetailsTimeFormatter {
    const val FIVE_MINUTES_IN_MILLIS = 5 * 60 * 1000
    fun toFormattedDateString(timeMillis: Long): String {
        val instant = kotlin.time.Instant.fromEpochMilliseconds(timeMillis)
        val currentTimeInstant = kotlin.time.Clock.System.now()
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val subtractedTimeMillis = currentTimeInstant.minus(instant).inWholeMilliseconds
        return if (subtractedTimeMillis in 0..FIVE_MINUTES_IN_MILLIS) {
            "Just now"
        } else {
            val day = date.dayOfMonth.toString().padStart(2, '0')
            val month = date.month.name.uppercase().take(3)
            val year = date.year
            val hour = date.hour.toString().padStart(2, '0')
            val minute = date.minute.toString().padStart(2, '0')

            "$day $month $year, $hour:$minute"
        }
    }
}
