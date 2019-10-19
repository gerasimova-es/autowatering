package com.home.autowatering.integration

import com.home.autowatering.Application
import com.home.autowatering.util.objectMapper
import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(VertxExtension::class)
abstract class BaseIntegrationTest {
//    protected val mapper = ObjectMapper().findAndRegisterModules()
//    protected val mapper = jacksonObjectMapper()
    protected val mapper = objectMapper()

    @BeforeEach
    fun init(vertx: Vertx, testContext: VertxTestContext) {
        vertx.deployVerticle(Application(), testContext.completing())
    }
}