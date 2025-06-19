package me.androidbox.core.presentation.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Long.toFormattedDate(): String {
    val timeZone = TimeZone.currentSystemDefault()
    val dateTime = Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(timeZone)
    val currentTimeInstant = Clock.System.now()
    val currentDateTime = Instant.fromEpochSeconds(currentTimeInstant.epochSeconds)
        .toLocalDateTime(timeZone)

    // Our date should look like this : (e.x) 25 APR or if its not current year i mean
    // in this case not 2025 then we should have format like : 25 APR 2024 for example

    val day = dateTime.dayOfMonth
    val monthAbbr = dateTime.month.name.uppercase().take(3)
    val year = dateTime.year

    return if(dateTime.year != currentDateTime.year) {
        "$day $monthAbbr $year"
    } else "$day $monthAbbr"
}