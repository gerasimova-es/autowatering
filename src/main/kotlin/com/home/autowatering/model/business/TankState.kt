package com.home.autowatering.model.business

import java.util.*

data class TankState(var id: Long? = null, val name: String, val date: Date, var volume: Double, val filled: Double)