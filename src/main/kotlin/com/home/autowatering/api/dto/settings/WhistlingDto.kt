package com.home.autowatering.api.dto.settings

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class WhistlingDto(
    var enabled: Boolean,
    var duration: Int,
    var startHour: Int,
    var startMinute: Int,
    var stopHour: Int,
    var stopMinute: Int
)