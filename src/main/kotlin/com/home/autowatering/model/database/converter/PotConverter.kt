package com.home.autowatering.model.database.converter

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.database.PotStateTable
import com.home.autowatering.model.database.PotTable.checkInterval
import com.home.autowatering.model.database.PotTable.code
import com.home.autowatering.model.database.PotTable.id
import com.home.autowatering.model.database.PotTable.minHumidity
import com.home.autowatering.model.database.PotTable.name
import com.home.autowatering.model.database.PotTable.wateringDuration
import org.jetbrains.exposed.sql.ResultRow

object PotConverter : Converter<ResultRow, Pot>(
    {
        Pot(
            id = it[id],
            code = it[code],
            name = it[name],
            minHumidity = it[minHumidity],
            checkInterval = it[checkInterval],
            wateringDuration = it[wateringDuration],
            //todo return humidity in every case
            humidity = if(it.data.size > 6) it[PotStateTable.humidity] else null
        )
    },
    {
        throw NotImplementedError() //todo
    }
)