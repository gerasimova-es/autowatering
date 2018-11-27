package com.home.autowatering.model

import java.util.*

data class TankState(
    var id: Long?, var name: String, var date: Date,
    var volume: Double, var filled: Double
) {
    constructor(name: String, date: Date, volume: Double, filled: Double) :
            this(null, name, java.sql.Date(date.time), volume, filled)
}