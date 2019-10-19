package com.home.autowatering.database

import com.home.autowatering.config.DatasourceConfig
import com.home.autowatering.exception.DatabaseAlreadyStartedException
import com.home.autowatering.exception.DatabaseNotStartedException
import com.opentable.db.postgres.embedded.EmbeddedPostgres

class EmbeddedDatabaseConnector(private val config: DatasourceConfig) : DatabaseConnector {
    private var instance: EmbeddedPostgres? = null

    override fun start() {
        if (instance != null) {
            throw DatabaseAlreadyStartedException()
        }
        instance = EmbeddedPostgres.builder()
            .setPort(8081) //todo port to tunes
            .setLocaleConfig("locale", "en-us")
            .setLocaleConfig("encoding", "UTF-8")
            .start()
    }

    override fun datasource() =
        instance?.getDatabase(config.user, "postgres")
            ?: throw DatabaseNotStartedException()

    override fun close() {
        instance?.close()
    }
}