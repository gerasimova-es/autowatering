package com.home.autowatering.model

data class WateringSettings(
    var enabled: Boolean,
    var minHumidity: Int,
    var interval: Int,
    var duration: Int
)