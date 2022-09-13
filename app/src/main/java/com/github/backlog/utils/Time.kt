package com.github.backlog.utils

import java.time.*

fun localDateFromEpochSecond(seconds: Long): LocalDate {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault())
        .toLocalDate()
}

fun LocalDate.toEpochSecond(): Long {
    return this.atStartOfDay(ZoneId.systemDefault())
        .toEpochSecond()
}

/**
 * Shorthand for Instant.now(Clock.systemDefaultZone()).epochSecond
 */
fun now(): Long {
    return Instant.now(Clock.systemDefaultZone()).epochSecond
}
