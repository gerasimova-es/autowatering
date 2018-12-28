package com.home.autowatering.dto

import java.util.*

class PotStateDto(val potName: String) {
    var id: Long? = null
    var date: Date? = null
    var humidity: Double? = null

    constructor(potName: String, date: Date, humidity: Double) : this(potName) {
        this.date = date
        this.humidity = humidity
    }

    constructor(id: Long, potName: String, date: Date, humidity: Double) : this(potName, date, humidity) {
        this.id = id
    }
}