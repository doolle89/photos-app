package com.example.ui.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
}

fun Long.toLocalDate(): LocalDate {
    return toLocalDateTime().date
}

fun Long.toLocalTime(): LocalTime {
    return toLocalDateTime().time
}

fun LocalDateTime.format(): String {
    val day = date.dayOfMonth
    val month = date.monthNumber
    val year = date.year
    return "${day}.${month}.${year}"
}