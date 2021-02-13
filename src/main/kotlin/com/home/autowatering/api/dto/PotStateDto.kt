package com.home.autowatering.api.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class PotStateDto {
    var id: Long? = null
    lateinit var potCode: String
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    var date: Date = Date()
    var humidity: Int = 0
    var watering: Boolean? = false

    constructor()

    constructor(
        id: Long? = null,
        potCode: String,
        date: Date,
        humidity: Int,
        watering: Boolean? = null
    ) {
        this.id = id
        this.potCode = potCode
        this.date = date
        this.humidity = humidity
        this.watering = watering
    }
}