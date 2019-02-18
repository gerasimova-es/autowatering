package com.home.autowatering.model.business

import java.util.*

data class PotState(
    var id: Long? = null,
    val pot: Pot,
    val date: Date,
    val humidity: Int,
    val watering: Boolean? = false
)