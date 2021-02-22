package com.home.autowatering.model.settings

import java.time.LocalTime

data class Lighting(
    val enabled: Boolean,
    val startTime: LocalTime,
    val stopTime: LocalTime
)