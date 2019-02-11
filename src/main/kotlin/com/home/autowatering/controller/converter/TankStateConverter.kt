package com.home.autowatering.controller.converter

import com.home.autowatering.controller.dto.TankStateDto
import com.home.autowatering.model.business.TankState
import java.util.*
import java.util.function.Function

object TankStateConverter : RequestConverter<TankStateDto, TankState>(
    Function {
        TankState(
            id = it.id,
            name = it.name,
            date = if (it.date == null) Date() else Date(it.date),
            volume = it.volume,
            filled = it.filled
        )
    },
    Function {
        TankStateDto(
            id = it.id,
            name = it.name,
            date = it.date.time,
            volume = it.volume,
            filled = it.filled
        )
    })