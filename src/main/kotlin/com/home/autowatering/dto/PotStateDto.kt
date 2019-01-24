package com.home.autowatering.dto

//todo date to Date
data class PotStateDto(val id: Long? = null, val potName: String, var date: Long? = null, var humidity: Double? = null)