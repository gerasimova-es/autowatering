package com.home.autowatering.converter

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import java.util.*
import java.util.function.Function

class PotStateConverter : RequestConverter<PotStateDto, PotState>(
    Function {
        PotState(
            id = it.id,
            pot = Pot(name = it.potName),
            date = Date(it.date!!),
            humidity = it.humidity!!
        )
    },
    Function {
        PotStateDto(
            id = it.id,
            potName = it.pot.name,
            date = it.date.time,
            humidity = it.humidity
        )
    }) {

    //todo to RequestConverter
    fun response(state: PotState): Response<PotStateDto> =
        Response(fromEntity(state))

    //todo to RequestConverter
    fun response(states: List<PotState>): Response<List<PotStateDto>> =
        Response(fromEntities(states))
}

