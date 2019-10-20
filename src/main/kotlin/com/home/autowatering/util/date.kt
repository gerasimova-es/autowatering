package com.home.autowatering.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val format = DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun String.toISODate(): ZonedDateTime =
    ZonedDateTime.from(
        format.parse(this)
    )

fun ZonedDateTime.toISODate(): String =
    this.format(format)
