package com.home.autowatering.api.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class WateringSettingsDto(
    var enabled: Boolean,
    var minHumidity: Int,
    var interval: Int,
    var duration: Int
)