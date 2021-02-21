package com.home.autowatering.model

import java.time.LocalTime

data class LightingSettings(
    var enabled: Boolean = true,
    var startTime: LocalTime,
    var stopTime: LocalTime
)