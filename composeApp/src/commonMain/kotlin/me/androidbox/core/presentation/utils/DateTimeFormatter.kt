@file:OptIn(ExperimentalTime::class)

package me.androidbox.core.presentation.utils

import co.touchlab.kermit.Logger
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
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

// reason":"1751065951816 is not a valid ISO timestamp in the format YYYY-MM-DDTHH:mm:ssZ.

fun Long.toIsoFormat(): String {
    val instant = kotlin.time.Instant.fromEpochMilliseconds(this)

    Logger.d { "$this => $instant" }

    return instant.toString()
}

fun String.toEpochMilliSeconds(): Long {
    return try {
        val instant = Instant.parse(this)

        val epochMs = instant.toEpochMilliseconds()

        Logger.d { "$this => $epochMs" }

        epochMs
    }
    catch (exception: IllegalArgumentException) {
        Logger.e {
            exception.message ?: ""
        }

        throw IllegalArgumentException("Failed to convert to epoch millis seconds")
    }
}

fun Long.toSyncFormattedDateTime(): String {
    val syncDateTimeResult = if(this == 0L) {
        "Never synced"
    }
    else {
        val syncDateTime = kotlin.time.Instant.fromEpochMilliseconds(this)
        val currentDateTime = Clock.System.now()
        val period = currentDateTime - syncDateTime

        if(period.inWholeMinutes<= 5) {
            "Just now"
        }
        else if(period.inWholeDays <= 7) {
            when  {
                period.inWholeMinutes <= 60.minutes.inWholeMinutes -> {
                   "${period.inWholeMinutes} minutes ago"
                }
                period.inWholeHours <= 24.hours.inWholeHours -> {
                    "${period.inWholeHours} hours ago"
                }
                else -> {
                    "${period.inWholeDays} days ago"
                }
            }
        }
        else {
            syncDateTime.toLocalDateTime(TimeZone.currentSystemDefault()).format(
                format = LocalDateTime.Format {
                    this.day()
                    this.chars(" ")
                    this.monthName(MonthNames.ENGLISH_ABBREVIATED)
                    this.chars(" ")
                    this.year(Padding.NONE)
                    this.minute(Padding.NONE)
                    this.chars(":")
                    this.hour(Padding.NONE)
                }
            )
        }
    }

    return syncDateTimeResult
}