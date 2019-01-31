package com.home.autowatering.model

data class Pot(var id: Long? = null, var code: String, var name: String? = null, val minHumidity: Int? = null)