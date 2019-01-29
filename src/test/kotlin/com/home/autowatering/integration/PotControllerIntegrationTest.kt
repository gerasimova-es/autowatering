package com.home.autowatering.integration

import com.home.autowatering.Application
import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.dto.response.ResponseStatus
import org.assertj.core.api.Assertions.assertThat
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
class PotControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun saveAndFind() {
        val dto = PotDto(
            code = "AUTHORIUM",
            name = "Ауториум"
        )

        val result = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(dto),
            object : ParameterizedTypeReference<Response<PotDto>>() {})

        assertThat(result).isNotNull
        assertThat(result.statusCode).isSameAs(HttpStatus.OK)
        assertThat(result.body).isNotNull
        assertThat(result.body?.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(result.body?.payload?.code).isEqualTo(dto.code)
        assertThat(result.body?.payload?.name).isEqualTo(dto.name)
        assertThat(result.body?.payload?.id).isGreaterThan(0)

        val builder = UriComponentsBuilder.fromPath("/pot/${result.body?.payload?.id}")
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
        assertThat(found.body?.payload?.code).isEqualTo(dto.code)
        assertThat(found.body?.payload?.name).isEqualTo(dto.name)
    }


    @Test
    fun savePotAndSaveStateAndHistory() {
        val potDto = PotDto(
            code = "pot1",
            name = "pot"
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
            name = "pot"
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