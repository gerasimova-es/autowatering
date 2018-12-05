package com.home.autowatering.model

import java.util.*

data class PotState(var id: Long?, var pot: Pot, var date: Date, var humidity: Double) {
    constructor(pot: Pot, date: Date, humidity: Double) : this(null, pot, date, humidity)
}