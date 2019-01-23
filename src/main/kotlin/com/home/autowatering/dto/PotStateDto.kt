package com.home.autowatering.dto

import java.util.*

data class PotStateDto(val potName: String, var date: Date? = null, var humidity: Double? = null)