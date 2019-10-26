package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PotDto @JsonCreator constructor(
    @field:JsonProperty("id")
    val id: Long? = null,
    @field:JsonProperty("code")
    val code: String,
    @field:JsonProperty("name")
    val name: String? = null,
    @field:JsonProperty("minHumidity")
    val minHumidity: Int? = null,
    @field:JsonProperty("checkInterval")
    val checkInterval: Int? = null,
    @field:JsonProperty("wateringDuration")
    val wateringDuration: Int? = null,
    @field:JsonProperty("humidity")
    var humidity: Int? = null
)