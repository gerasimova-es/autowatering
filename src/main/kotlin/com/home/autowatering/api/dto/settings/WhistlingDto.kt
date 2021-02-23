package com.home.autowatering.api.dto.settings

class WhistlingDto(
    var enabled: Boolean,
    var duration: Int,
    var startHour: Int,
    var startMinute: Int,
    var stopHour: Int,
    var stopMinute: Int
)