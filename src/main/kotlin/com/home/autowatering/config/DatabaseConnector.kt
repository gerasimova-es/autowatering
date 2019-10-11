package com.home.autowatering.config

import org.jetbrains.exposed.sql.Database

class DatabaseConnector(private val jdbc: JDBC) {
    private var database: Database? = null

    fun connect() {
        database = Database.connect(
            url = jdbc.url!!,
            driver = jdbc.driver!!,
            user = jdbc.user!!,
            password = jdbc.password!!
        )
    }
}