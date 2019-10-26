package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.ZonedDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PotStateDto @JsonCreator constructor(
    @field:JsonProperty("id")
    val id: Long? = null,
    @field:JsonProperty("potCode")
    val potCode: String? = null,
    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    )
    @field:JsonProperty("date")
    val date: ZonedDateTime = ZonedDateTime.now(),
    @field:JsonProperty("humidity")
    val humidity: Int = 0,
    @field:JsonProperty("watering")
    val watering: Boolean? = false
)