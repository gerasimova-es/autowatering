package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class TankStateDto {
    var id: Long? = null
    lateinit var name: String
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    var date: Date = Date()
    var volume: Double = 0.0
    var filled: Double = 0.0

    constructor()

    constructor(id: Long? = null, name: String, date: Date, volume: Double, filled: Double) {
        this.id = id
        this.name = name
        this.date = date
        this.volume = volume
        this.filled = filled
    }
}