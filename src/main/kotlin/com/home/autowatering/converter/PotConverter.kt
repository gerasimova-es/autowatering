package com.home.autowatering.converter

import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import java.util.function.Function

class PotConverter : RequestConverter<PotDto, Pot>(
    Function {
        Pot(
            id = it.id,
            code = it.code,
            name = it.name
        )
    },
    Function {
        PotDto(
            id = it.id,
            code = it.code,
            name = it.name
        )
    }
) {
    fun response(pot: Pot, state: PotState? = null): Response<PotDto> {
        if (state == null) {
            return super.response(pot)
        }
        val dto = fromEntity(pot)
        dto.humidity = state.humidity
        return Response(dto)
    }
}