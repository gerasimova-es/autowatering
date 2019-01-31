package com.home.autowatering.dto

data class PotDto(
    val id: Long? = null,
    val code: String,
    val name: String? = null,
    var minHumidity: Int? = null,
    var humidity: Double? = null
)