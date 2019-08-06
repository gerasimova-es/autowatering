package com.home.autowatering.model.database

import org.jetbrains.exposed.sql.Table

object PotTable : Table(name = "pot") {
    val id = long("id").autoIncrement().primaryKey()
    var code = varchar("code", 100).uniqueIndex()
    var name = varchar("name", 255)
    var minHumidity = integer("min_humidity")
    var checkInterval = integer("check_interval")
    var wateringDuration = integer("watering_duration")
}