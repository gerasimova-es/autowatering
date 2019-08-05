package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class TankStateDto(
    val id: Long? = null,
    val name: String,
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    val date: Date = Date(),
    val volume: Double = 0.0,
    val filled: Double = 0.0
)