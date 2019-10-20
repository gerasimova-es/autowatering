package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class UserDto(
    var login: String? = null,
    var password: String? = null
)