package com.home.autowatering.model.database.converter

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.database.PotStateTable
import com.home.autowatering.model.database.PotTable
import org.jetbrains.exposed.sql.ResultRow

object PotStateConverter : Converter<ResultRow, PotState>(
    {
        PotState(
            id = it[PotStateTable.id],
            humidity = it[PotStateTable.humidity],
            date = it[PotStateTable.date].toGregorianCalendar().toZonedDateTime(),
            watering = it[PotStateTable.watering],
            pot = Pot(
                id = it[PotStateTable.pot],
                code = it[PotTable.code],
                name = it[PotTable.name],
                minHumidity = it[PotTable.minHumidity],
                wateringDuration = it[PotTable.wateringDuration],
                checkInterval = it[PotTable.checkInterval]
            )
        )
    },
    {
        throw NotImplementedError() //todo
    }
)