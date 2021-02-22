package com.home.autowatering.model.settings

import java.time.LocalTime

data class Whistling(
    val enabled: Boolean,
    val duration: Int,
    val startTime: LocalTime,
    val stopTime: LocalTime
)