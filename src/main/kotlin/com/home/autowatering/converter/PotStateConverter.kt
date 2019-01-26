package com.home.autowatering.converter

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.model.PotState
import java.util.*
import java.util.function.Function

class PotStateConverter : RequestConverter<PotStateDto, PotState>(
    Function {
        PotState(
            id = it.id,
            date = Date(it.date),
            humidity = it.humidity
        )
    },
    Function {
        PotStateDto(
            id = it.id,
            date = it.date.time,
            humidity = it.humidity
        )
    })

