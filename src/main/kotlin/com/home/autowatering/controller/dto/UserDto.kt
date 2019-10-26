package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto @JsonCreator constructor (
    @field:JsonProperty("login")
    val login: String? = null,
    @field:JsonProperty("password")
    val password: String? = null
)