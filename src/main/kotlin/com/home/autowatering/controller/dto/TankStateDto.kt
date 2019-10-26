package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TankStateDto @JsonCreator constructor(
    @field:JsonProperty("id")
    val id: Long? = null,
    @field:JsonProperty("name")
    val name: String,
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    @field:JsonProperty("date")
    val date: Date = Date(),
    @field:JsonProperty("volume")
    val volume: Double = 0.0,
    @field:JsonProperty("filled")
    val filled: Double = 0.0
)