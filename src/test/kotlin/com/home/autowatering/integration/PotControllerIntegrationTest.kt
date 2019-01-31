package com.home.autowatering.integration

import com.home.autowatering.Application
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.PotStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.ResponseStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [Application::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Ignore
class PotControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun saveAndFind() {
        val autorium = PotDto(
            code = "AUTHORIUM",
            name = "Ауториум",
            minHumidity = 400,
            checkInterval = 10,
            wateringDuration = 2
        )

        val savedAutorium = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(autorium),
            object : ParameterizedTypeReference<Response<PotDto>>() {})

        assertThat(savedAutorium).isNotNull
        assertThat(savedAutorium.statusCode).isSameAs(HttpStatus.OK)
        assertThat(savedAutorium.body).isNotNull
        assertThat(savedAutorium.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(savedAutorium.body?.payload?.id).isGreaterThan(0)
        assertThat(savedAutorium.body?.payload?.code).isEqualTo(autorium.code)
        assertThat(savedAutorium.body?.payload?.name).isEqualTo(autorium.name)
        assertThat(savedAutorium.body?.payload?.minHumidity).isEqualTo(autorium.minHumidity)
        assertThat(savedAutorium.body?.payload?.checkInterval).isEqualTo(autorium.checkInterval)
        assertThat(savedAutorium.body?.payload?.wateringDuration).isEqualTo(autorium.wateringDuration)

        val phalaenopsis = PotDto(
            code = "PHALAENOPSIS",
            name = "Фаленопсис",
            minHumidity = 300,
            checkInterval = 10,
            wateringDuration = 2
        )

        val savedPhalaenopsis = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(phalaenopsis),
            object : ParameterizedTypeReference<Response<PotDto>>() {})

        assertThat(savedPhalaenopsis).isNotNull
        assertThat(savedPhalaenopsis.statusCode).isSameAs(HttpStatus.OK)
        assertThat(savedPhalaenopsis.body).isNotNull
        assertThat(savedPhalaenopsis.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(savedPhalaenopsis.body?.payload?.id).isGreaterThan(0)
        assertThat(savedPhalaenopsis.body?.payload?.code).isEqualTo(phalaenopsis.code)
        assertThat(savedPhalaenopsis.body?.payload?.name).isEqualTo(phalaenopsis.name)
        assertThat(savedPhalaenopsis.body?.payload?.minHumidity).isEqualTo(phalaenopsis.minHumidity)
        assertThat(savedPhalaenopsis.body?.payload?.checkInterval).isEqualTo(phalaenopsis.checkInterval)
        assertThat(savedPhalaenopsis.body?.payload?.wateringDuration).isEqualTo(phalaenopsis.wateringDuration)

        val eucharis = PotDto(
            code = "EUCHARIS",
            name = "Эухарис",
            minHumidity = 400,
            checkInterval = 11,
            wateringDuration = 3
        )

        val savedEucharis = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(eucharis),
            object : ParameterizedTypeReference<Response<PotDto>>() {})

        assertThat(savedEucharis).isNotNull
        assertThat(savedEucharis.statusCode).isSameAs(HttpStatus.OK)
        assertThat(savedEucharis.body).isNotNull
        assertThat(savedEucharis.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(savedEucharis.body?.payload?.id).isGreaterThan(0)
        assertThat(savedEucharis.body?.payload?.code).isEqualTo(eucharis.code)
        assertThat(savedEucharis.body?.payload?.name).isEqualTo(eucharis.name)
        assertThat(savedEucharis.body?.payload?.minHumidity).isEqualTo(eucharis.minHumidity)
        assertThat(savedEucharis.body?.payload?.checkInterval).isEqualTo(eucharis.checkInterval)
        assertThat(savedEucharis.body?.payload?.wateringDuration).isEqualTo(eucharis.wateringDuration)

        val builder = UriComponentsBuilder.fromPath("/pot/${savedAutorium.body?.payload?.id}")
        val found = restTemplate.exchange(
            builder.build().encode().toUri(),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<Response<PotDto>>() {}
        )
        assertThat(found.statusCode).isSameAs(HttpStatus.OK)
        assertThat(found.body).isNotNull
        assertThat(found.body?.message).isEqualTo("message was handled successfully")
        assertThat(found.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(found.body?.payload).isInstanceOf(PotDto::class.java)
        assertThat(found.body?.payload?.code).isEqualTo(autorium.code)
        assertThat(found.body?.payload?.name).isEqualTo(autorium.name)
        assertThat(found.body?.payload?.minHumidity).isEqualTo(autorium.minHumidity)
        assertThat(found.body?.payload?.checkInterval).isEqualTo(autorium.checkInterval)
        assertThat(found.body?.payload?.wateringDuration).isEqualTo(autorium.wateringDuration)
    }


    @Test
    fun savePotAndSaveStateAndHistory() {
        val potDto = PotDto(
            code = "pot1",
            name = "pot",
            minHumidity = 100,
            checkInterval = 1,
            wateringDuration = 1
        )

        val savedPotDto = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(potDto),
            object : ParameterizedTypeReference<Response<PotDto>>() {})

        assertThat(savedPotDto.statusCode).isSameAs(HttpStatus.OK)
        assertThat(savedPotDto.body).isNotNull
        assertThat(savedPotDto.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(savedPotDto.body?.payload?.id).isGreaterThan(0)


        val stateDto = PotStateDto(
            potCode = "pot1",
            humidity = 100.0,
            watering = true
        )
        val savedStateDto = restTemplate.exchange(
            "/pot/state/save",
            HttpMethod.POST,
            HttpEntity(stateDto),
            object : ParameterizedTypeReference<Response<PotStateDto>>() {})

        assertThat(savedStateDto.statusCode).isSameAs(HttpStatus.OK)
        assertThat(savedStateDto.body).isNotNull
        assertThat(savedStateDto.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(savedStateDto.body?.payload?.id).isGreaterThan(0)
        assertThat(savedStateDto.body?.payload?.watering).isTrue()

        val builder = UriComponentsBuilder.fromPath("/pot/${savedPotDto?.body?.payload?.id}/state/list")

        val found = restTemplate.exchange(
            builder.build().encode().toUri(),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<Response<List<PotStateDto>>>() {}
        )
        assertThat(found.statusCode).isSameAs(HttpStatus.OK)
        assertThat(found.body).isNotNull
        assertThat(found.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(found.body?.message).isEqualTo("message was handled successfully")
        assertThat(found.body?.payload).isInstanceOf(ArrayList::class.java)
        //todo check elements
    }

    @Test
    fun savePotAndSaveStateAndFind() {
        val potDto = PotDto(
            code = "pot_savePotAndSaveStateAndFind",
            name = "pot",
            minHumidity = 100,
            checkInterval = 1,
            wateringDuration = 1
        )

        val savedPotDto = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(potDto),
            object : ParameterizedTypeReference<Response<PotDto>>() {})

        assertThat(savedPotDto.statusCode).isSameAs(HttpStatus.OK)
        assertThat(savedPotDto.body).isNotNull
        assertThat(savedPotDto.body?.status).isSameAs(ResponseStatus.SUCCESS)

        val stateDto = PotStateDto(
            potCode = "pot_savePotAndSaveStateAndFind",
            humidity = 100.0
        )

        val savedStateDto = restTemplate.exchange(
            "/pot/state/save",
            HttpMethod.POST,
            HttpEntity(stateDto),
            object : ParameterizedTypeReference<Response<PotStateDto>>() {})

        assertThat(savedStateDto.statusCode).isSameAs(HttpStatus.OK)
        assertThat(savedStateDto.body).isNotNull
        assertThat(savedStateDto.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(savedStateDto.body?.payload?.id).isGreaterThan(0)

        val builder = UriComponentsBuilder.fromPath("/pot/${savedPotDto.body?.payload?.id}")
        val found = restTemplate.exchange(
            builder.build().encode().toUri(),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<Response<PotDto>>() {}
        )
        assertThat(found.statusCode).isSameAs(HttpStatus.OK)
        assertThat(found.body).isNotNull
        assertThat(found.body?.message).isEqualTo("message was handled successfully")
        assertThat(found.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(found.body?.payload).isInstanceOf(PotDto::class.java)
        assertThat(found.body?.payload?.code).isEqualTo(savedPotDto.body?.payload?.code)
        assertThat(found.body?.payload?.name).isEqualTo(savedPotDto.body?.payload?.name)
        assertThat(found.body?.payload?.humidity).isEqualTo(stateDto.humidity)
    }
}