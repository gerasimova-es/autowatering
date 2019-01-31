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
            minHumidity = it.minHumidity,
            checkInterval = it.checkInterval,
            wateringDuration = it.wateringDuration
        )
    },
    Function {
        JpaPot(
            id = it.id,
            code = it.code,
            name = it.name,
            minHumidity = it.minHumidity,
            checkInterval = it.checkInterval,
            wateringDuration = it.wateringDuration
        )
    }

) {

    fun map(source: Pot, target: JpaPot): JpaPot {
        target.code = source.code
        target.name = source.name
        target.minHumidity = source.minHumidity
        target.checkInterval = source.checkInterval
        target.wateringDuration = source.wateringDuration
        return target
    }
}