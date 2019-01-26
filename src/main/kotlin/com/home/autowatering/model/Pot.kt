package com.home.autowatering.model

data class Pot(var id: Long? = null, var name: String, var description: String? = null, var state: PotState? = null)