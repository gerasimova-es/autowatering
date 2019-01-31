package com.home.autowatering.controller.converter

import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import java.util.function.Function

class PotConverter : RequestConverter<PotDto, Pot>(
    Function {
        Pot(
            id = it.id,
            code = it.code,
            name = it.name,
            minHumidity = it.minHumidity,
            checkInterval = it.checkInterval,
            wateringDuration = it.wateringDuration
        )
    },
    Function {
        PotDto(
            id = it.id,
            code = it.code,
            name = it.name,
            minHumidity = it.minHumidity,
            checkInterval = it.checkInterval,
            wateringDuration = it.wateringDuration
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