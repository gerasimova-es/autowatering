package com.home.autowatering.model.database

import org.jetbrains.exposed.sql.Table

object TankStateTable : Table("tank_state") {
    var id = long("id").autoIncrement().primaryKey()
    var name = varchar("code", 100)
    var date = datetime("date").index()
    var volume = decimal(name = "volume", precision = 2, scale = 10)
    var filled = decimal(name = "filled", precision = 2, scale = 10)
}