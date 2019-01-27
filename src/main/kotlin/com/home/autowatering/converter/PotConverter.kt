package com.home.autowatering.converter

import com.home.autowatering.dto.PotDto
import com.home.autowatering.model.Pot
import java.util.function.Function

class PotConverter : RequestConverter<PotDto, Pot>(
    Function {
        Pot(
            id = it.id,
            code = it.code,
            name = it.name,
            humidity = it.humidity
        )
    },
    Function {
        PotDto(
            id = it.id,
            code = it.code,
            name = it.name,
            humidity = it.humidity
        )
    }
)