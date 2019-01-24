package com.home.autowatering.integration

import com.home.autowatering.controller.PotStateController
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
class PotStateControllerIntegrationTest {
    companion object {
        const val DATE_FORMAT = PotStateController.DATE_FORMAT
    }

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun save() {
        val dto = PotStateDto(
            potName = "pot",
            date = Date().time,
            humidity = 1.0
        )

        val result = restTemplate.exchange(
            "/potstate/save",
            HttpMethod.POST,
            HttpEntity(dto),
            object : ParameterizedTypeReference<Response<PotStateDto>>() {})

        assertThat(result).isNotNull
        assertThat(result.statusCode).isSameAs(HttpStatus.OK)
        assertThat(result.body).isNotNull
        assertThat(result.body!!.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(result.body!!.payload!!.potName).isEqualTo(dto.potName)
        assertThat(result.body!!.payload!!.date).isEqualTo(dto.date)
        assertThat(result.body!!.payload!!.humidity).isEqualTo(dto.humidity)
        assertThat(result.body!!.payload!!.id).isGreaterThan(0)
    }

    @Test
    fun saveAndFind() {
        val dto1 = PotStateDto(
            potName = "pot",
            date = Date().time,
            humidity = 1.0
        )

        val saved1 = restTemplate.exchange(
            "/potstate/save",
            HttpMethod.POST,
            HttpEntity(dto1),
            object : ParameterizedTypeReference<Response<PotStateDto>>() {})

        assertThat(saved1.statusCode).isSameAs(HttpStatus.OK)

        val dto2 = PotStateDto(
            potName = "pot",
            date = Date().time,
            humidity = 2.0
        )

        val saved2 = restTemplate.exchange(
            "/potstate/save",
            HttpMethod.POST,
            HttpEntity(dto2),
            object : ParameterizedTypeReference<Response<PotStateDto>>() {})

        assertThat(saved2.statusCode).isSameAs(HttpStatus.OK)

        val builder = UriComponentsBuilder.fromPath("/potstate/list")
            .queryParam("potName", "pot")
            .queryParam("dateFrom", SimpleDateFormat(DATE_FORMAT).format(Date(dto1.date!!)))
            .queryParam("dateTo", SimpleDateFormat(DATE_FORMAT).format(Date(dto2.date!!)))

        val found = restTemplate.exchange(
            builder.build().encode().toUri(),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<Response<List<PotStateDto>>>() {}
        )
        assertThat(found.statusCode).isSameAs(HttpStatus.OK)
        assertThat(found.body).isNotNull
        assertThat(found.body!!.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(found.body!!.message).isEqualTo("message was handled successfully")
        assertThat(found.body!!.payload).isInstanceOf(ArrayList::class.java)
        assertThat(found.body!!.payload).hasSize(2)
        assertThat(found.body!!.payload).hasOnlyElementsOfType(PotStateDto::class.java)
        //todo check elements
    }
}