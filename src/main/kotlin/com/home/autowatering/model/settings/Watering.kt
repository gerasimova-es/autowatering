package com.home.autowatering.model.settings

data class Watering(
    val enabled: Boolean,
    val minHumidity: Int,
    val checkInterval: Int,
    val duration: Int
)