package com.home.autowatering.database

import com.home.autowatering.config.DatabaseConfig
import com.home.autowatering.config.DatabaseMode
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import java.sql.Connection
import javax.sql.DataSource

object Database {
    fun connect(datasource: () -> DataSource): () -> Connection =
        org.jetbrains.exposed.sql.Database
            .connect(datasource.invoke())
            .connector

    fun fill(connection: () -> Connection) {
        Liquibase(
            "db/liquibase/changelog.json",
            ClassLoaderResourceAccessor(),
            DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(
                    JdbcConnection(connection.invoke())
                )
        ).update("postgres")
    }
}

fun datasource(config: DatabaseConfig): () -> DataSource {
    val source = config.datasource!!

    return when (config.mode!!) {
        DatabaseMode.EMBEDDED -> {
            {
                EmbeddedPostgres.builder()
                    .setPort(8081) //todo port to tunes
                    .start()
                    .getDatabase(source.user, "postgres")

            }
        }
        DatabaseMode.REMOTE -> {
            {
                HikariDataSource(
                    HikariConfig().also {
                        it.jdbcUrl = source.url
                        it.driverClassName = source.driver
                        it.username = source.user
                        it.password = source.password
                    })
            }
        }
    }
}





