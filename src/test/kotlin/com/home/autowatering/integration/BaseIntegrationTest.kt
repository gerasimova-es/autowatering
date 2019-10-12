package com.home.autowatering.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.home.autowatering.Application
import com.opentable.db.postgres.embedded.EmbeddedPostgres
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
abstract class BaseIntegrationTest {
    protected val mapper = jacksonObjectMapper()

    @BeforeEach
    fun init(vertx: Vertx, testContext: VertxTestContext) {
        val postgres = EmbeddedPostgres.builder()
            .setPort(8081)
            .start()

        val database = DatabaseFactory.getInstance()
            .findCorrectDatabaseImplementation(
                JdbcConnection(postgres.postgresDatabase.connection)
            )

        val liquibase = Liquibase(
            "db/liquibase/changelog.json",
            ClassLoaderResourceAccessor(),
            database
        )

        liquibase.update("test")

        vertx.deployVerticle(Application(), testContext.completing())
    }
}