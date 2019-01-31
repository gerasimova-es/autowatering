package com.home.autowatering.controller.dto

import java.util.*

//todo date to Date
data class PotStateDto(
    val id: Long? = null,
    val potCode: String,
    val date: Long? = Date().time,
    val humidity: Double,
    val watering: Boolean? = false
)