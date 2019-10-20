package com.home.autowatering.integration

import com.home.autowatering.config.EndPoint
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.PotStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.StatusType
import com.home.autowatering.util.mapper
import com.home.autowatering.util.toISODate
import com.home.autowatering.util.toJson
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.client.WebClient
import io.vertx.reactivex.ext.web.codec.BodyCodec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.net.URLEncoder
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class PotControllerIntegrationTest : BaseIntegrationTest() {
    private val port = 8080

    private val potListType = with(mapper) {
        typeFactory.constructParametricType(
            Response::class.java,
            typeFactory.constructCollectionType(
                List::class.java,
                PotDto::class.java
            )
        )
    }
    private val potType = with(mapper) {
        typeFactory.constructParametricType(
            Response::class.java,
            PotDto::class.java
        )
    }
    private val potStateType = with(mapper) {
        typeFactory.constructParametricType(
            Response::class.java,
            PotStateDto::class.java
        )
    }

    @Test
    @Timeout(value = 120, timeUnit = TimeUnit.SECONDS)
    fun list(vertx: Vertx, testContext: VertxTestContext) {
        val client = WebClient.create(vertx)

        client.get(port, "localhost", EndPoint.POT_LIST.path)
            .`as`(BodyCodec.jsonObject())
            .send(testContext.succeeding { response ->
                testContext.verify {
                    val result = mapper.readValue<Response<List<PotDto>>>(response.body().encode(), potListType)
                    assertThat(result.payload).hasSizeGreaterThanOrEqualTo(2)
                    result.payload?.get(0).also { element ->
                        assertThat(element?.id).isNotNull()
                        assertThat(element?.code).isEqualTo("AUTHORIUM")
                        assertThat(element?.name).isEqualTo("Ауториум")
                        assertThat(element?.minHumidity).isEqualTo(200)
                        assertThat(element?.checkInterval).isEqualTo(60)
                        assertThat(element?.wateringDuration).isEqualTo(2)
                    }
                    result.payload?.get(1).also { element ->
                        assertThat(element?.id).isNotNull()
                        assertThat(element?.code).isEqualTo("ORCHID")
                        assertThat(element?.name).isEqualTo("Орхидея")
                        assertThat(element?.minHumidity).isEqualTo(250)
                        assertThat(element?.checkInterval).isEqualTo(120)
                        assertThat(element?.wateringDuration).isEqualTo(1)
                    }
                    testContext.completeNow()
                }
            })
    }

    @Test
    @Timeout(value = 120, timeUnit = TimeUnit.SECONDS)
    fun info(vertx: Vertx, testContext: VertxTestContext) {
        val client = WebClient.create(vertx)

        client.get(port, "localhost", "${EndPoint.POT_INFO.path}?code=AUTHORIUM")
            .`as`(BodyCodec.jsonObject())
            .send(testContext.succeeding { response ->
                testContext.verify {
                    val result = mapper.readValue<Response<PotDto>>(response.body().encode(), potType)
                    assertThat(result.payload?.code).isEqualTo("AUTHORIUM")
                    assertThat(result.payload?.name).isEqualTo("Ауториум")
                    assertThat(result.payload?.minHumidity).isEqualTo(200)
                    assertThat(result.payload?.checkInterval).isEqualTo(60)
                    assertThat(result.payload?.wateringDuration).isEqualTo(2)
                    testContext.completeNow()
                }
            })
    }

    @Test
    @Timeout(value = 120, timeUnit = TimeUnit.SECONDS)
    fun saveNewPot(vertx: Vertx, testContext: VertxTestContext) {
        val dto = PotDto(
            code = "TEST",
            name = "test",
            minHumidity = 400,
            checkInterval = 10,
            wateringDuration = 2
        )

        val client = WebClient.create(vertx)

        client.post(port, "localhost", EndPoint.POT_SAVE.path)
            .`as`(BodyCodec.jsonObject())
            .sendJsonObject(dto.toJson(), testContext.succeeding { response ->
                testContext.verify {
                    val result = mapper.readValue<Response<PotDto>>(response.body().encode(), potType)
                    assertNotNull(result.payload?.id)
                    assertThat(result.payload?.code).isEqualTo("TEST")
                    assertThat(result.payload?.name).isEqualTo("test")
                    assertThat(result.payload?.minHumidity).isEqualTo(400)
                    assertThat(result.payload?.checkInterval).isEqualTo(10)
                    assertThat(result.payload?.wateringDuration).isEqualTo(2)
                    testContext.completeNow()
                }
            })
    }

    @Test
    @Timeout(value = 120, timeUnit = TimeUnit.SECONDS)
    fun saveState(vertx: Vertx, testContext: VertxTestContext) {
        val dto = PotStateDto(
            potCode = "AUTHORIUM",
            humidity = 100,
            watering = true,
            date = ZonedDateTime.now()
        )

        val client = WebClient.create(vertx)

        client.post(port, "localhost", EndPoint.POT_STATE_SAVE.path)
            .`as`(BodyCodec.jsonObject())
            .sendJsonObject(dto.toJson(), testContext.succeeding { response ->
                testContext.verify {
                    val result = mapper.readValue<Response<PotStateDto>>(response.body().encode(), potStateType)
                    assertThat(result.status).isSameAs(StatusType.SUCCESS)
                    testContext.completeNow()
                }
            })
    }

    @Test
    @Timeout(value = 120, timeUnit = TimeUnit.SECONDS)
    fun getStatistic(vertx: Vertx, testContext: VertxTestContext) {

        val date = ZonedDateTime.now()
        val dto = PotStateDto(
            potCode = "AUTHORIUM",
            humidity = 100,
            watering = true,
            date = date
        )

        val client = WebClient.create(vertx)

        client.post(port, "localhost", EndPoint.POT_STATE_SAVE.path)
            .`as`(BodyCodec.jsonObject())
            .sendJsonObject(dto.toJson(), testContext.succeeding { response ->
                testContext.verify {
                    val result = mapper.readValue<Response<PotStateDto>>(response.body().encode(), potStateType)
                    assertThat(result.status).isSameAs(StatusType.SUCCESS)
                    testContext.completeNow()
                }
            })

        client.get(
            port,
            "localhost",
            URLEncoder.encode("/pot/statistic?code=AUTHORIUM&dateFrom=${date.toISODate()}&dateTo=${date.toISODate()}")
        ).`as`(BodyCodec.jsonObject())
            .send(testContext.succeeding { response ->
                testContext.verify {
                    val result = mapper.readValue<Response<PotDto>>(response.body().encode(), potType)
                    assertNotNull(result.payload?.id)
                    assertThat(result.status).isSameAs(StatusType.SUCCESS)
                    testContext.completeNow()
                }
            })
    }
}
