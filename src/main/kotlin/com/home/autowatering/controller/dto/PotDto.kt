package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class PotDto {
    var id: Long? = null
    lateinit var code: String
    var name: String? = null
    var minHumidity: Int? = null
    var checkInterval: Int? = null
    var wateringDuration: Int? = null
    var humidity: Double? = null

    constructor()
    constructor(
        id: Long? = null,
        code: String,
        name: String? = null,
        minHumidity: Int? = null,
        checkInterval: Int? = null,
        wateringDuration: Int? = null,
        humidity: Double? = null
    ) {
        this.id = id
        this.code = code
        this.name = name
        this.minHumidity = minHumidity
        this.checkInterval = checkInterval
        this.wateringDuration = wateringDuration
        this.humidity = humidity
    }


}