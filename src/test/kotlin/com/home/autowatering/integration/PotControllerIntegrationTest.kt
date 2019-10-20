package com.home.autowatering.integration

import com.home.autowatering.config.EndPoint
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.PotStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.StatusType
import com.home.autowatering.util.mapper
import com.home.autowatering.util.toJson
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.client.WebClient
import io.vertx.reactivex.ext.web.codec.BodyCodec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
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
            .sendJson(dto.toJson(), testContext.succeeding { response ->
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
        val dto = PotStateDto(
            potCode = "AUTHORIUM",
            humidity = 100,
            watering = true,
            date = ZonedDateTime.now()
        )

        val client = WebClient.create(vertx)

        client.post(port, "localhost", EndPoint.POT_STATE_SAVE.path)
            .`as`(BodyCodec.jsonObject())
            .sendJson(dto, testContext.succeeding { response ->
                testContext.verify {
                    val result = mapper.readValue<Response<PotStateDto>>(response.body().encode(), potStateType)
                    assertThat(result.status).isSameAs(StatusType.SUCCESS)
                    testContext.completeNow()
                }
            })

        client.get(port, "localhost", "/pot/statistic/AUTHORIUM?dateFrom=&dateTo=")
            .`as`(BodyCodec.jsonObject())
            .sendJson(dto, testContext.succeeding { response ->
                testContext.verify {
                    val result = mapper.readValue<Response<PotDto>>(response.body().encode(), potType)
                    assertNotNull(result.payload?.id)
                    assertThat(result.status).isSameAs(StatusType.SUCCESS)
                    testContext.completeNow()
                }
            })
    }

//    @Autowired
//    lateinit var restTemplate: TestRestTemplate
//
//    @Test
//    fun savePotWithNotExistedId() {
//        val dto = PotDto(
//            id = 100000000000000,
//            code = "TEST",
//            name = "test",
//            minHumidity = 400,
//            checkInterval = 10,
//            wateringDuration = 2
//        )
//
//        val savedAuthorium = restTemplate.exchange(
//            "/pot/save",
//            HttpMethod.POST,
//            HttpEntity(dto),
//            object : ParameterizedTypeReference<Response<PotDto>>() {})
//
//        assertThat(savedAuthorium).isNotNull
//        assertThat(savedAuthorium.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savedAuthorium.body).isNotNull
//        assertThat(savedAuthorium.body?.status).isSameAs(ResponseStatus.ERROR)
//    }
//
//    @Test
//    fun saveAndUpdate() {
//        val dto1 = PotDto(
//            code = "TEST1",
//            name = "test1",
//            minHumidity = 100,
//            checkInterval = 10,
//            wateringDuration = 2
//        )
//
//        val savePot1 = restTemplate.exchange(
//            "/pot/save",
//            HttpMethod.POST,
//            HttpEntity(dto1),
//            object : ParameterizedTypeReference<Response<PotDto>>() {})
//
//        assertThat(savePot1).isNotNull
//        assertThat(savePot1.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savePot1.body).isNotNull
//        assertThat(savePot1.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(savePot1.body?.payload?.id).isGreaterThan(0)
//        assertThat(savePot1.body?.payload?.code).isEqualTo(dto1.code)
//        assertThat(savePot1.body?.payload?.name).isEqualTo(dto1.name)
//        assertThat(savePot1.body?.payload?.minHumidity).isEqualTo(dto1.minHumidity)
//        assertThat(savePot1.body?.payload?.checkInterval).isEqualTo(dto1.checkInterval)
//        assertThat(savePot1.body?.payload?.wateringDuration).isEqualTo(dto1.wateringDuration)
//
//        val dto2 = PotDto(
//            id = savePot1.body!!.payload!!.id,
//            code = "TEST2",
//            name = "test2",
//            minHumidity = 150,
//            checkInterval = 13,
//            wateringDuration = 3
//        )
//
//        val savePot2 = restTemplate.exchange(
//            "/pot/save",
//            HttpMethod.POST,
//            HttpEntity(dto2),
//            object : ParameterizedTypeReference<Response<PotDto>>() {})
//
//        assertThat(savePot2).isNotNull
//        assertThat(savePot2.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savePot2.body).isNotNull
//        assertThat(savePot2.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(savePot2.body?.payload?.id).isGreaterThan(0)
//        assertThat(savePot2.body?.payload?.code).isEqualTo(dto2.code)
//        assertThat(savePot2.body?.payload?.name).isEqualTo(dto2.name)
//        assertThat(savePot2.body?.payload?.minHumidity).isEqualTo(dto2.minHumidity)
//        assertThat(savePot2.body?.payload?.checkInterval).isEqualTo(dto2.checkInterval)
//        assertThat(savePot2.body?.payload?.wateringDuration).isEqualTo(dto2.wateringDuration)
//
//    }
//
//    @Test
//    fun saveAndFind() {
//        val autorium = PotDto(
//            code = "AUTHORIUM",
//            name = "Ауториум",
//            minHumidity = 400,
//            checkInterval = 10,
//            wateringDuration = 2
//        )
//
//        val savedAutorium = restTemplate.exchange(
//            "/pot/save",
//            HttpMethod.POST,
//            HttpEntity(autorium),
//            object : ParameterizedTypeReference<Response<PotDto>>() {})
//
//        assertThat(savedAutorium).isNotNull
//        assertThat(savedAutorium.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savedAutorium.body).isNotNull
//        assertThat(savedAutorium.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(savedAutorium.body?.payload?.id).isGreaterThan(0)
//        assertThat(savedAutorium.body?.payload?.code).isEqualTo(autorium.code)
//        assertThat(savedAutorium.body?.payload?.name).isEqualTo(autorium.name)
//        assertThat(savedAutorium.body?.payload?.minHumidity).isEqualTo(autorium.minHumidity)
//        assertThat(savedAutorium.body?.payload?.checkInterval).isEqualTo(autorium.checkInterval)
//        assertThat(savedAutorium.body?.payload?.wateringDuration).isEqualTo(autorium.wateringDuration)
//
//        val phalaenopsis = PotDto(
//            code = "PHALAENOPSIS",
//            name = "Фаленопсис",
//            minHumidity = 300,
//            checkInterval = 10,
//            wateringDuration = 2
//        )
//
//        val savedPhalaenopsis = restTemplate.exchange(
//            "/pot/save",
//            HttpMethod.POST,
//            HttpEntity(phalaenopsis),
//            object : ParameterizedTypeReference<Response<PotDto>>() {})
//
//        assertThat(savedPhalaenopsis).isNotNull
//        assertThat(savedPhalaenopsis.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savedPhalaenopsis.body).isNotNull
//        assertThat(savedPhalaenopsis.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(savedPhalaenopsis.body?.payload?.id).isGreaterThan(0)
//        assertThat(savedPhalaenopsis.body?.payload?.code).isEqualTo(phalaenopsis.code)
//        assertThat(savedPhalaenopsis.body?.payload?.name).isEqualTo(phalaenopsis.name)
//        assertThat(savedPhalaenopsis.body?.payload?.minHumidity).isEqualTo(phalaenopsis.minHumidity)
//        assertThat(savedPhalaenopsis.body?.payload?.checkInterval).isEqualTo(phalaenopsis.checkInterval)
//        assertThat(savedPhalaenopsis.body?.payload?.wateringDuration).isEqualTo(phalaenopsis.wateringDuration)
//
//        val eucharis = PotDto(
//            code = "EUCHARIS",
//            name = "Эухарис",
//            minHumidity = 400,
//            checkInterval = 11,
//            wateringDuration = 3
//        )
//
//        val savedEucharis = restTemplate.exchange(
//            "/pot/save",
//            HttpMethod.POST,
//            HttpEntity(eucharis),
//            object : ParameterizedTypeReference<Response<PotDto>>() {})
//
//        assertThat(savedEucharis).isNotNull
//        assertThat(savedEucharis.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savedEucharis.body).isNotNull
//        assertThat(savedEucharis.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(savedEucharis.body?.payload?.id).isGreaterThan(0)
//        assertThat(savedEucharis.body?.payload?.code).isEqualTo(eucharis.code)
//        assertThat(savedEucharis.body?.payload?.name).isEqualTo(eucharis.name)
//        assertThat(savedEucharis.body?.payload?.minHumidity).isEqualTo(eucharis.minHumidity)
//        assertThat(savedEucharis.body?.payload?.checkInterval).isEqualTo(eucharis.checkInterval)
//        assertThat(savedEucharis.body?.payload?.wateringDuration).isEqualTo(eucharis.wateringDuration)
//
//        val builder = UriComponentsBuilder.fromPath("/pot/${savedAutorium.body?.payload?.id}")
//        val found = restTemplate.exchange(
//            builder.build().encode().toUri(),
//            HttpMethod.GET,
//            null,
//            object : ParameterizedTypeReference<Response<PotDto>>() {}
//        )
//        assertThat(found.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(found.body).isNotNull
//        assertThat(found.body?.message).isEqualTo("message was handled successfully")
//        assertThat(found.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(found.body?.payload).isInstanceOf(PotDto::class.java)
//        assertThat(found.body?.payload?.code).isEqualTo(autorium.code)
//        assertThat(found.body?.payload?.name).isEqualTo(autorium.name)
//        assertThat(found.body?.payload?.minHumidity).isEqualTo(autorium.minHumidity)
//        assertThat(found.body?.payload?.checkInterval).isEqualTo(autorium.checkInterval)
//        assertThat(found.body?.payload?.wateringDuration).isEqualTo(autorium.wateringDuration)
//    }
//
//
//    @Test
//    fun savePotAndSaveStateAndHistory() {
//        val potDto = PotDto(
//            code = "pot1",
//            name = "pot",
//            minHumidity = 100,
//            checkInterval = 1,
//            wateringDuration = 1
//        )
//
//        val savedPotDto = restTemplate.exchange(
//            "/pot/save",
//            HttpMethod.POST,
//            HttpEntity(potDto),
//            object : ParameterizedTypeReference<Response<PotDto>>() {})
//
//        assertThat(savedPotDto.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savedPotDto.body).isNotNull
//        assertThat(savedPotDto.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(savedPotDto.body?.payload?.id).isGreaterThan(0)
//
//
//        val stateDto = PotStateDto(
//            potCode = "pot1",
//            humidity = 100,
//            watering = true,
//            date = Date()
//        )
//        val savedStateDto = restTemplate.exchange(
//            "/pot/state/save",
//            HttpMethod.POST,
//            HttpEntity(stateDto),
//            object : ParameterizedTypeReference<Response<PotStateDto>>() {})
//
//        assertThat(savedStateDto.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savedStateDto.body).isNotNull
//        assertThat(savedStateDto.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(savedStateDto.body?.payload?.id).isGreaterThan(0)
//        assertThat(savedStateDto.body?.payload?.watering).isTrue()
//
//        val builder = UriComponentsBuilder.fromPath("/pot/statistic/${savedPotDto?.body?.payload?.code}")
//
//        val found = restTemplate.exchange(
//            builder.build().encode().toUri(),
//            HttpMethod.GET,
//            null,
//            object : ParameterizedTypeReference<Response<List<PotStateDto>>>() {}
//        )
//        assertThat(found.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(found.body).isNotNull
//        assertThat(found.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(found.body?.message).isEqualTo("message was handled successfully")
//        assertThat(found.body?.payload).isInstanceOf(ArrayList::class.java)
//        assertThat(found.body?.payload).hasSize(1)
//        //todo check elements
//    }
//
//    @Test
//    fun savePotAndSaveStateAndFind() {
//        val potDto = PotDto(
//            code = "pot_savePotAndSaveStateAndFind",
//            name = "pot",
//            minHumidity = 100,
//            checkInterval = 1,
//            wateringDuration = 1
//        )
//
//        val savedPotDto = restTemplate.exchange(
//            "/pot/save",
//            HttpMethod.POST,
//            HttpEntity(potDto),
//            object : ParameterizedTypeReference<Response<PotDto>>() {})
//
//        assertThat(savedPotDto.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savedPotDto.body).isNotNull
//        assertThat(savedPotDto.body?.status).isSameAs(ResponseStatus.SUCCESS)
//
//        val stateDto = PotStateDto(
//            potCode = "pot_savePotAndSaveStateAndFind",
//            humidity = 100,
//            date = Date()
//        )
//
//        val savedStateDto = restTemplate.exchange(
//            "/pot/state/save",
//            HttpMethod.POST,
//            HttpEntity(stateDto),
//            object : ParameterizedTypeReference<Response<PotStateDto>>() {})
//
//        assertThat(savedStateDto.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(savedStateDto.body).isNotNull
//        assertThat(savedStateDto.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(savedStateDto.body?.payload?.id).isGreaterThan(0)
//
//        val builder = UriComponentsBuilder.fromPath("/pot/${savedPotDto.body?.payload?.id}")
//            .uriVariables(mapOf("dateFrom" to stateDto.date, "dateTo" to stateDto.date))
//
//        val found = restTemplate.exchange(
//            builder.build().encode().toUri(),
//            HttpMethod.GET,
//            null,
//            object : ParameterizedTypeReference<Response<PotDto>>() {}
//        )
//        assertThat(found.statusCode).isSameAs(HttpStatus.OK)
//        assertThat(found.body).isNotNull
//        assertThat(found.body?.message).isEqualTo("message was handled successfully")
//        assertThat(found.body?.status).isSameAs(ResponseStatus.SUCCESS)
//        assertThat(found.body?.payload).isInstanceOf(PotDto::class.java)
//        assertThat(found.body?.payload?.code).isEqualTo(savedPotDto.body?.payload?.code)
//        assertThat(found.body?.payload?.name).isEqualTo(savedPotDto.body?.payload?.name)
//    }
}
