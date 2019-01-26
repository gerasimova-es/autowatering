package com.home.autowatering.converter

import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import java.util.*
import java.util.function.Function

class PotConverter : RequestConverter<PotDto, Pot>(
    Function {
        Pot(
            id = it.id,
            name = it.name,
            description = it.description,
            state =
            if (it.state == null) null
            else PotState(
                id = it.state.id,
                date = Date(it.state.date),
                humidity = it.state.humidity

            )
        )
    },
    Function {
        PotDto(
            id = it.id,
            name = it.name,
            description = it.description,
            state =
            if (it.state == null) null
            else PotStateDto(
                id = it.state!!.id,
                date = it.state!!.date.time,
                humidity = it.state!!.humidity
            )

        )
    }
)