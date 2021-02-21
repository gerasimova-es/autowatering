package com.home.autowatering.model

import java.time.LocalTime

data class WhistlingSettings(
    var enabled: Boolean,
    var duration: Int,
    var startTime: LocalTime,
    var stopTime: LocalTime
)