package com.home.autowatering.model.business

data class Pot(
    var id: Long? = null,
    var code: String,
    var name: String? = null,
    var minHumidity: Int? = null,
    var checkInterval: Int? = null,
    var wateringDuration: Int? = null,
    var humidity: Int? = 0
)