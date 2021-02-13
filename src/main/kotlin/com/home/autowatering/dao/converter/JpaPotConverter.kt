package com.home.autowatering.dao.converter

import com.home.autowatering.model.business.Pot
import com.home.autowatering.dao.entity.JpaPot

object JpaPotConverter : JpaConverter<JpaPot, Pot>(
    {
        Pot(
            id = it.id,
            code = it.code!!,
            name = it.name,
            minHumidity = it.minHumidity,
            checkInterval = it.checkInterval,
            wateringDuration = it.wateringDuration
        )
    },
    {
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