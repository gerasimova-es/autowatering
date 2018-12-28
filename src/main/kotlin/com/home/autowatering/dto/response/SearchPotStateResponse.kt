package com.home.autowatering.dto.response

import com.home.autowatering.dto.PotStateDto

class SearchPotStateResponse : Response<List<PotStateDto>> {
    constructor(states: List<PotStateDto>) : super(states)
    constructor(status: ResponseStatus, message: String) : super(status, message)
}
