package com.home.autowatering.model.settings

data class Vaporizer(
    var enabled: Boolean,
    var minHumidity: Int,
    var checkInterval: Int
)