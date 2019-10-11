package com.home.autowatering.integration

import io.vertx.core.Vertx
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
class ApplicationTest {
    @Test
    fun startHttpServer() {
        val testContext = VertxTestContext()
        val vertx = Vertx.vertx()
        vertx.createHttpServer()
            .requestHandler { req -> req.response().end() }
            .listen(16969, testContext.completing())

        assertThat(testContext.awaitCompletion(5, TimeUnit.SECONDS)).isTrue()
        if (testContext.failed()) {
            throw testContext.causeOfFailure()
        }
    }
}