package com.home.autowatering.integration

import com.home.autowatering.Application
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
abstract class BaseIntegrationTest {
    private val application : Application = Application()

    @BeforeEach
    fun init(vertx: Vertx, testContext: VertxTestContext) {
        vertx.deployVerticle(application, testContext.completing())
    }

    @AfterEach
    fun close(){
        application.database?.close()
    }
}