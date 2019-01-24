package com.home.autowatering.converter

import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.Pot
import java.util.function.Function

class PotConverter : RequestConverter<PotDto, Pot>(
    Function {
        Pot(
            id = it.id,
            name = it.name,
            description = it.description
        )
    },
    Function {
        PotDto(
            id = it.id,
            name = it.name,
            description = it.description
        )
    }
) {
    //todo to RequestConverter
    fun response(pot: Pot): Response<PotDto> =
        Response(fromEntity(pot))
}