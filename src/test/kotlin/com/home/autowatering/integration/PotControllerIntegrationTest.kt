package com.home.autowatering.integration

import com.home.autowatering.controller.PotController.Companion.DATE_FORMAT
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
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PotControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun saveWithoutStateAndFind() {
        val dto = PotDto(
            name = "pot",
            description = "pot"
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
        assertThat(result.body?.payload?.name).isEqualTo(dto.name)
        assertThat(result.body?.payload?.description).isEqualTo(dto.description)
        assertThat(result.body?.payload?.id).isGreaterThan(0)

        val id = result.body?.payload?.id

        val builder = UriComponentsBuilder.fromPath("/pot/$id")
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
        assertThat(found.body?.payload?.id).isEqualTo(id)
        assertThat(found.body?.payload?.name).isEqualTo(dto.name)
        assertThat(found.body?.payload?.name).isEqualTo(dto.description)
    }

    @Test
    fun saveWithStateAndFind() {
        val dto = PotDto(
            name = "pot",
            description = "pot",
            state = PotStateDto(
                date = Date().time,
                humidity = 1.0
            )
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
        assertThat(result.body?.payload?.id).isGreaterThan(0)
        assertThat(result.body?.payload?.name).isEqualTo(dto.name)
        assertThat(result.body?.payload?.description).isEqualTo(dto.description)
        assertThat(result.body?.payload?.state).isNotNull
        assertThat(result.body?.payload?.state?.id).isGreaterThan(0)
        assertThat(result.body?.payload?.state?.date).isEqualTo(dto.state?.date)
        assertThat(result.body?.payload?.state?.humidity).isEqualTo(dto.state?.humidity)

        val potId = result.body?.payload?.id
        val stateId = result.body?.payload?.state?.id

        val builder = UriComponentsBuilder.fromPath("/pot/$potId")
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
        assertThat(found.body?.payload?.id).isEqualTo(potId)
        assertThat(found.body?.payload?.name).isEqualTo(dto.name)
        assertThat(found.body?.payload?.name).isEqualTo(dto.description)
        assertThat(found.body?.payload?.state).isNotNull
        assertThat(found.body?.payload?.state?.id).isEqualTo(stateId)
        assertThat(found.body?.payload?.state?.date).isEqualTo(dto.state?.date)
        assertThat(found.body?.payload?.state?.humidity).isEqualTo(dto.state?.humidity)
    }


    @Test
    fun saveAndStateHistory() {
        val dto1 = PotDto(
            name = "pot",
            description = "pot",
            state = PotStateDto(
                date = Date().time,
                humidity = 1.0
            )
        )

        val saved1 = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(dto1),
            object : ParameterizedTypeReference<Response<PotStateDto>>() {})

        assertThat(saved1.statusCode).isSameAs(HttpStatus.OK)

        val dto2 = PotDto(
            name = "pot",
            description = "pot",
            state = PotStateDto(
                date = Date().time,
                humidity = 2.0
            )
        )

        val saved2 = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(dto2),
            object : ParameterizedTypeReference<Response<PotStateDto>>() {})

        assertThat(saved2.statusCode).isSameAs(HttpStatus.OK)
        assertThat(saved1?.body?.payload?.id).isEqualTo(saved2?.body?.payload?.id)

        val potId = saved1?.body?.payload?.id;

        val builder = UriComponentsBuilder.fromPath("/pot/$potId/states")
            .queryParam("dateFrom", SimpleDateFormat(DATE_FORMAT).format(Date(dto1.state?.date!!)))
            .queryParam("dateTo", SimpleDateFormat(DATE_FORMAT).format(Date(dto2.state?.date!!)))

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
        assertThat(found.body?.payload).hasSize(2)
        assertThat(found.body?.payload).hasOnlyElementsOfType(PotStateDto::class.java)
        //todo check elements
    }
}