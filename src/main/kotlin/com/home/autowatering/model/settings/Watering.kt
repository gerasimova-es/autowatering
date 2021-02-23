package com.home.autowatering.model.settings

data class Watering(
    var id: Long? = null,
    var enabled: Boolean,
    var minHumidity: Int,
    var checkInterval: Int,
    var duration: Int
)