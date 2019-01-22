package com.home.autowatering.converter

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import java.util.function.Function

class PotStateConverter : RequestConverter<PotStateDto, PotState>(
    Function {
        PotState(
            pot = Pot(name = it.potName),
            date = it.date!!,
            humidity = it.humidity!!
        )
    },
    Function {
        PotStateDto(
            it.id!!,
            it.pot.name,
            it.date,
            it.humidity
        )
    }) {

    //todo to RequestConverter
    fun response(state: PotState): Response<PotStateDto> =
        Response(fromEntity(state))

    //todo to RequestConverter
    fun response(states: List<PotState>): Response<List<PotStateDto>> =
        Response(fromEntities(states))
}

