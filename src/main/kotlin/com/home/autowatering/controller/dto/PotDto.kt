package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PotDto(
    val id: Long? = null,
    val code: String,
    val name: String? = null,
    val minHumidity: Int? = null,
    val checkInterval: Int? = null,
    val wateringDuration: Int? = null,
    var humidity: Int? = null
)