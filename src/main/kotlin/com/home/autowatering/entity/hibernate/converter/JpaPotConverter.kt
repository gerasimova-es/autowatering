package com.home.autowatering.entity.hibernate.converter

import com.home.autowatering.entity.hibernate.JpaPot
import com.home.autowatering.model.Pot
import java.util.function.Function

class JpaPotConverter : JpaConverter<JpaPot, Pot>(
    Function {
        Pot(
            id = it.id,
            code = it.code!!,
            name = it.name,
            minHumidity = it.minHumidity
        )
    },
    Function {
        JpaPot(
            id = it.id,
            name = it.code,
            description = it.name,
            minHumidity = it.minHumidity
        )
    }

) {

    fun map(source: Pot, target: JpaPot): JpaPot {
        target.code = source.code
        target.name = source.name
        return target
    }
}