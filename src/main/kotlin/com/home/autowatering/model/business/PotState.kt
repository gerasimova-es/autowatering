package com.home.autowatering.model.business

import java.time.ZonedDateTime

data class PotState(
    var id: Long? = null,
    val pot: Pot,
    val date: ZonedDateTime,
    val humidity: Int,
    val watering: Boolean? = false
)