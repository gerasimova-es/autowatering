package com.home.autowatering.controller.converter

import com.home.autowatering.controller.dto.PotStateDto
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState

object PotStateConverter : RequestConverter<PotStateDto, PotState>(
    {
        PotState(
            id = it.id,
            pot = Pot(code = it.potCode),
            date = it.date,
            humidity = it.humidity,
            watering = it.watering

        )
    },
    {
        PotStateDto(
            id = it.id,
            potCode = it.pot.code,
            date = it.date,
            humidity = it.humidity,
            watering = it.watering
        )
    })

