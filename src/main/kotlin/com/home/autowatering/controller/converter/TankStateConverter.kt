package com.home.autowatering.controller.converter

import com.home.autowatering.controller.dto.TankStateDto
import com.home.autowatering.model.business.TankState

object TankStateConverter : RequestConverter<TankStateDto, TankState>(
    {
        TankState(
            id = it.id,
            name = it.name,
            date = it.date,
            volume = it.volume,
            filled = it.filled
        )
    },
    {
        TankStateDto(
            id = it.id,
            name = it.name,
            date = it.date,
            volume = it.volume,
            filled = it.filled
        )
    })