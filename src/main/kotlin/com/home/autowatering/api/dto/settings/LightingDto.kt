package com.home.autowatering.api.dto.settings

class LightingDto(
    var enabled: Boolean = true,
    var startHour: Int,
    var startMinute: Int,
    var stopHour: Int,
    var stopMinute: Int
)