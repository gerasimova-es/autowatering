package com.home.autowatering.model.settings

data class Vaporizer(
    var id: Long? = null,
    var enabled: Boolean,
    var minHumidity: Int,
    var checkInterval: Int
)