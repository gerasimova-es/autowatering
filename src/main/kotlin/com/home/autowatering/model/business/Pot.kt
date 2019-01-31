package com.home.autowatering.model.business

data class Pot(
    var id: Long? = null,
    var code: String,
    var name: String? = null,
    var minHumidity: Int? = 300,
    var checkInterval: Int? = 10,
    var wateringDuration: Int? = 2
)