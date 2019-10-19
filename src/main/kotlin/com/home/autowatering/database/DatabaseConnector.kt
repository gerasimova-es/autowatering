package com.home.autowatering.database

import com.home.autowatering.config.DatabaseConfig
import com.home.autowatering.config.DatabaseMode
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import javax.sql.DataSource

interface DatabaseConnector {
    fun start()
    fun datasource(): DataSource
    fun close()
}

fun database(config: DatabaseConfig) =
    when (config.mode) {
        DatabaseMode.EMBEDDED -> EmbeddedDatabaseConnector(config.datasource!!)
        DatabaseMode.REMOTE -> RemoteDatabaseConnector(config.datasource!!)
        else -> throw IllegalArgumentException("incorrect mode ${config.mode}")
    }

fun DataSource.connect() =
    org.jetbrains.exposed.sql.Database
        .connect(this)

fun DataSource.fill() {
    this.connection.use { connection ->
        Liquibase(
            "db/liquibase/changelog.json",
            ClassLoaderResourceAccessor(),
            DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(
                    JdbcConnection(connection)
                )
        ).update("postgres")
    }
}