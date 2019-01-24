package com.home.autowatering.converter

import com.home.autowatering.dto.TankStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.TankState
import java.util.*
import java.util.function.Function

class TankStateConverter : RequestConverter<TankStateDto, TankState>(
    Function {
        TankState(id = it.id, name = it.name, date = Date(it.date), volume = it.volume, filled = it.filled)
    },
    Function {
        TankStateDto(id = it.id, name = it.name, date = it.date.time, volume = it.volume, filled = it.filled)
    }) {
    //todo to RequestConverter
    fun response(state: TankState): Response<TankStateDto> =
        Response(fromEntity(state))
}