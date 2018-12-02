package com.home.autowatering.dto.response

import com.home.autowatering.dto.PotStateDto

class SarchPotStateResponse(val states: List<PotStateDto>) : BaseResponse<List<PotStateDto>>() {
}