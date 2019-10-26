package com.home.autowatering.controller.converter

import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState

object PotConverter : RequestConverter<PotDto, Pot>(
    {
        Pot(
            id = it.id,
            code = it.code,
            name = it.name,
            minHumidity = it.minHumidity,
            checkInterval = it.checkInterval,
            wateringDuration = it.wateringDuration
        )
    },
    {
        PotDto(
            id = it.id,
            code = it.code,
            name = it.name,
            minHumidity = it.minHumidity,
            checkInterval = it.checkInterval,
            wateringDuration = it.wateringDuration,
            humidity = it.humidity
        )
    }
) {
    fun response(pot: Pot, state: PotState? = null): Response<PotDto> {
        if (state == null) {
            return super.response(pot)
        }
        return Response(fromEntity(pot).also {
            it.humidity = state.humidity
        })
    }
}