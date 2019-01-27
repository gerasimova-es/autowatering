package com.home.autowatering.converter

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import java.util.*
import java.util.function.Function

class PotStateConverter : RequestConverter<PotStateDto, PotState>(
    Function {
        PotState(
            id = it.id,
            pot = Pot(code = it.potCode),
            date = if (it.date == null) Date() else Date(it.date),
            humidity = it.humidity,
            watering = it.watering

        )
    },
    Function {
        PotStateDto(
            id = it.id,
            potCode = it.pot.code,
            date = it.date.time,
            humidity = it.humidity,
            watering = it.watering
        )
    })

