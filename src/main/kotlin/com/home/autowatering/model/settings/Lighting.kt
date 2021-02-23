package com.home.autowatering.model.settings

import java.time.LocalTime

data class Lighting(
    var enabled: Boolean,
    var startTime: LocalTime,
    var stopTime: LocalTime
)