package com.home.autowatering.dto

data class PotDto(
    val id: Long? = null,
    val code: String,
    val name: String? = null,
    var minHumidity: Int? = null,
    var checkInterval: Int? = null,
    var wateringDuration: Int? = null,
    var humidity: Double? = null
)