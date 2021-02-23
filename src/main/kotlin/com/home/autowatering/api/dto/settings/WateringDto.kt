package com.home.autowatering.api.dto.settings

class WateringDto(
    var enabled: Boolean,
    var minHumidity: Int,
    var interval: Int,
    var duration: Int
)