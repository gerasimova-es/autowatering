package com.home.autowatering.model.settings

import java.time.LocalTime

data class Whistling(
    var id: Long? = null,
    var enabled: Boolean,
    var duration: Int,
    var startTime: LocalTime,
    var stopTime: LocalTime
)