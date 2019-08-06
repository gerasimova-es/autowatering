package com.home.autowatering.model.database

import org.jetbrains.exposed.sql.Table

object PotStateTable : Table("pot_state") {
    val id = long("id").autoIncrement().primaryKey()
    var date = datetime("date")
    var pot = reference("pot_id", PotTable.id)
    var humidity = integer("humidity")
    var watering = bool("watering")

    init {
        index(false, date, pot)
    }
}