package com.home.autowatering.database

import com.home.autowatering.config.DatasourceConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class RemoteDatabaseConnector(private val config: DatasourceConfig) : DatabaseConnector {
    override fun start() {}

    override fun datasource() =
        HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = config.url
                driverClassName = config.driver
                username = config.user
                password = config.password
            })

    override fun close() {}
}