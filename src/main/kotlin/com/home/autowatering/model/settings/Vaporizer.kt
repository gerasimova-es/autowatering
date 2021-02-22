package com.home.autowatering.model.settings

data class Vaporizer(
    val enabled: Boolean,
    val minHumidity: Int,
    val checkInterval: Int
)