package com.home.autowatering.integration

import com.home.autowatering.controller.dto.TankStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.ResponseStatus
import org.assertj.core.api.Assertions
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
import java.util.*

@Ignore
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TankStateControllerIntegrationTest {
    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun save() {
        val dto = TankStateDto(
            name = "tank",
            volume = 2.178,
            filled = 0.0,
            date = Date()
        )

        val result = restTemplate.exchange(
            "/tank/state/save",
            HttpMethod.POST,
            HttpEntity(dto),
            object : ParameterizedTypeReference<Response<TankStateDto>>() {})

        Assertions.assertThat(result).isNotNull
        Assertions.assertThat(result.statusCode).isSameAs(HttpStatus.OK)
        Assertions.assertThat(result.body).isNotNull
        Assertions.assertThat(result.body!!.status).isSameAs(ResponseStatus.SUCCESS)
        Assertions.assertThat(result.body!!.payload!!.id).isGreaterThan(0)
        Assertions.assertThat(result.body!!.payload!!.name).isEqualTo(dto.name)
        Assertions.assertThat(result.body!!.payload!!.volume).isEqualTo(dto.volume)
    }
}