package com.home.autowatering.model

import java.util.*

data class PotState(var id: Long? = null, var pot: Pot, val date: Date, val humidity: Double)