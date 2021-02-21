package com.home.autowatering.model

data class VaporizeSettings(
    var enabled: Boolean,
    var minHumidity: Int,
    var interval: Int
)