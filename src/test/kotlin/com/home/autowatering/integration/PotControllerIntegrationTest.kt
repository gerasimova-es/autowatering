package com.home.autowatering.integration

import com.home.autowatering.dto.PotDto
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

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PotControllerIntegrationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun save() {
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
        assertThat(result.body!!.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(result.body!!.payload!!.name).isEqualTo(dto.name)
        assertThat(result.body!!.payload!!.description).isEqualTo(dto.description)
        assertThat(result.body!!.payload!!.id).isGreaterThan(0)
    }

    @Test
    fun saveAndFind() {
        val dto1 = PotDto(
            name = "pot1",
            description = "pot1"
        )

        val saved1 = restTemplate.exchange(
            "/pot/save",
            HttpMethod.POST,
            HttpEntity(dto1),
            object : ParameterizedTypeReference<Response<PotDto>>() {})

        assertThat(saved1.statusCode).isSameAs(HttpStatus.OK)

        val id = saved1.body!!.payload!!.id

        val builder = UriComponentsBuilder.fromPath("/pot/$id")

        val found = restTemplate.exchange(
            builder.build().encode().toUri(),
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<Response<PotDto>>() {}
        )
        assertThat(found.statusCode).isSameAs(HttpStatus.OK)
        assertThat(found.body).isNotNull
        assertThat(found.body!!.message).isEqualTo("message was handled successfully")
        assertThat(found.body!!.status).isSameAs(ResponseStatus.SUCCESS)
        assertThat(found.body!!.payload).isInstanceOf(PotDto::class.java)
        assertThat(found.body!!.payload!!.id).isEqualTo(id)
        assertThat(found.body!!.payload!!.name).isEqualTo(dto1.name)
        assertThat(found.body!!.payload!!.name).isEqualTo(dto1.description)
    }
}