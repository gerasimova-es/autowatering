package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


@JsonInclude(JsonInclude.Include.NON_NULL)
data class TokensDto @JsonCreator constructor(
    @field:JsonProperty("accessToken")
    val accessToken: String? = null,
    @field:JsonProperty("refreshToken")
    val refreshToken: String? = null
)