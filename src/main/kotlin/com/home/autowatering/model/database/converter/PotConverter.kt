package com.home.autowatering.model.database.converter

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.database.PotTable
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
            wateringDuration = it[wateringDuration]
        )
    },
    {
        //todo
        ResultRow(1, emptyMap())
//        PotTable(
//            id = it.id,
//            code = it.code,
//            name = it.name,
//            minHumidity = it.minHumidity,
//            checkInterval = it.checkInterval,
//            wateringDuration = it.wateringDuration
//        )
    }

) {

    fun map(source: Pot, target: PotTable): PotTable =
        //todo
        PotTable
//        target.also {
//            it.code = source.code
//            it.name = source.name
//            it.minHumidity = source.minHumidity
//            it.checkInterval = source.checkInterval
//            it.wateringDuration = source.wateringDuration
//        }
}