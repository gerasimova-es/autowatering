package com.home.autowatering.integration

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.home.autowatering.Application
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
abstract class BaseIntegrationTest {
    protected val mapper = jacksonObjectMapper()

    @BeforeEach
    fun init(vertx: Vertx, testContext: VertxTestContext) {
        vertx.deployVerticle(Application(), testContext.completing())
    }
}