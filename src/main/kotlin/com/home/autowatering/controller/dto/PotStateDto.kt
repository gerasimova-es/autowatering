package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.ZonedDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PotStateDto(
    var id: Long? = null,
    var potCode: String? = null,
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    var date: ZonedDateTime = ZonedDateTime.now(),
    var humidity: Int = 0,
    var watering: Boolean? = false
)