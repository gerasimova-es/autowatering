package com.home.autowatering.model.database.converter

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.database.PotStateTable
import java.util.*

object JpaPotStateConverter : Converter<PotStateTable, PotState>(
    {
        PotState(pot = Pot(code = "1"), date = Date(), humidity = 1)
//        PotState(
//            id = it.id,
//            pot = PotConverter.fromJpa(it.pot!!),
//            date = it.date!!,
//            humidity = it.humidity!!,
//            watering = it.watering

    },
    {
        PotStateTable
//        PotStateTable(
//            id = it.id,
//            pot = PotConverter.fromEntity(it.pot),
//            date = it.date,
//            humidity = it.humidity,
//            watering = it.watering
//        )
    }
)