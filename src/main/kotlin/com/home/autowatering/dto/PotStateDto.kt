package com.home.autowatering.dto

import java.util.*

data class PotStateDto(var id: Long? = null, val potName: String, var date: Date? = null, var humidity: Double? = null)