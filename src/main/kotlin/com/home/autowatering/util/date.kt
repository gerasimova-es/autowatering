package com.home.autowatering.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun String.toISODate() =
    ZonedDateTime.from(
        DateTimeFormatter.ISO_DATE_TIME.parse(this))
