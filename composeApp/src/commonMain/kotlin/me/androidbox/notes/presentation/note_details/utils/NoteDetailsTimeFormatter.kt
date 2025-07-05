package me.androidbox.notes.presentation.note_details.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toDateTimePeriod
import kotlinx.datetime.toLocalDateTime

object NoteDetailsTimeFormatter {
    fun toFormattedDateString(timeMillis: Long): String {
        val instant = Instant.fromEpochMilliseconds(timeMillis)
        val currentTimeInstant = Clock.System.now()
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val subtractedTimeMillis = currentTimeInstant.minus(instant).inWholeMilliseconds
        return if (subtractedTimeMillis in 0..5 * 60 * 1000) {
            "Just now"
        } else {
            "${date.dayOfMonth.toString().padStart(2, '0')} ${date.month.name.take(3)} " +
                    "${date.year}, ${date.hour}:${date.minute}"
        }
    }
}
