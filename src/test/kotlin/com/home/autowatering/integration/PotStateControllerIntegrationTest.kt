package com.home.autowatering.integration

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
import java.util.*


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PotStateControllerIntegrationTest {

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
        assertThat(result.body!!.status).isSameAs(ResponseStatus.SUCCESS) //todo figure out about generic
        assertThat(result.body!!.payload!!.potName).isEqualTo(dto.potName)
        assertThat(result.body!!.payload!!.date).isEqualTo(dto.date)
        assertThat(result.body!!.payload!!.humidity).isEqualTo(dto.humidity)
        assertThat(result.body!!.payload!!.id).isGreaterThan(0)
    }

    @Test
    fun saveAndFind() {
        val dto = PotStateDto(
            potName = "pot",
            date = Date().time,
            humidity = 1.0
        )

        val saved = restTemplate.exchange(
            "/potstate/save",
            HttpMethod.POST,
            HttpEntity(dto),
            object : ParameterizedTypeReference<Response<PotStateDto>>() {})

//        val found = restTemplate.exchange(
//            "/potstate/list",
//            HttpMethod.GET,
//
//        )

        assertThat(saved).isNotNull
        assertThat(saved.statusCode).isSameAs(HttpStatus.OK)
        assertThat(saved.body).isNotNull
        assertThat(saved.body!!.status).isSameAs(ResponseStatus.SUCCESS) //todo figure out about generic
        assertThat(saved.body!!.payload!!.potName).isEqualTo(dto.potName)
        assertThat(saved.body!!.payload!!.date).isEqualTo(dto.date)
        assertThat(saved.body!!.payload!!.humidity).isEqualTo(dto.humidity)
        assertThat(saved.body!!.payload!!.id).isGreaterThan(0)

    }
}