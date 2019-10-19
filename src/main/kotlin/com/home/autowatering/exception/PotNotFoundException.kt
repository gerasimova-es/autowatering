package com.home.autowatering.exception

import com.home.autowatering.controller.dto.response.StatusType

class PotNotFoundException : AutowateringException {
    constructor(id: Long) : super(StatusType.POT_NOT_FOUND, "pot not found by id = $id")
    constructor(code: String) : super(StatusType.POT_NOT_FOUND, "pot not found by code = $code")
}