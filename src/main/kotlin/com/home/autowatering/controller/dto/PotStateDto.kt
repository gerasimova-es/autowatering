package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PotStateDto(
    val id: Long? = null,
    val potCode: String,
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    val date: Date = Date(),
    val humidity: Int = 0,
    val watering: Boolean? = false
)