package com.home.autowatering.model

import java.util.*

data class PotState(
    var id: Long? = null,
    val pot: Pot,
    val date: Date,
    val humidity: Double,
    val watering: Boolean? = false
)