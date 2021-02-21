package com.home.autowatering.api.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class LightingSettingsDto(
    var enabled: Boolean = true,
    var startHour: Int,
    var startMinute: Int,
    var stopHour: Int,
    var stopMinute: Int
)