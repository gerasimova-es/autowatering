package com.home.autowatering.model.database.converter

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.database.JpaPot
import java.util.function.Function

object JpaPotConverter : JpaConverter<JpaPot, Pot>(
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

    fun map(source: Pot, target: JpaPot): JpaPot =
        target.also {
            it.code = source.code
            it.name = source.name
            it.minHumidity = source.minHumidity
            it.checkInterval = source.checkInterval
            it.wateringDuration = source.wateringDuration
        }
}