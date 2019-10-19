package com.home.autowatering.exception

import com.home.autowatering.controller.dto.response.StatusType

abstract class AutowateringException(
    val status: StatusType,
    override val message: String? = null
) : RuntimeException(message)