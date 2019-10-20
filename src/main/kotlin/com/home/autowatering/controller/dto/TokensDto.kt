package com.home.autowatering.controller.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class TokensDto(
    var accessToken: String? = null,
    var refreshToken: String? = null
)