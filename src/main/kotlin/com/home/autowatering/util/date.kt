package com.home.autowatering.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun String.ISODate() =
    ZonedDateTime.from(
        DateTimeFormatter.ISO_DATE_TIME.parse(this))
