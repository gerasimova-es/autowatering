package com.home.autowatering.integration

import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PotControllerIntegrationTest {

//    @Autowired
//    lateinit var restTemplate: TestRestTemplate
//
//    @Test
//    fun save() {
//        val dto = PotStateDto(
//            potName = "pot",
//            date = Date().time,
//            humidity = 1.0
//        )
//
//        val result = restTemplate.exchange(
//            "/potstate/save",
//            HttpMethod.POST,
//            HttpEntity(dto),
//            object : ParameterizedTypeReference<Response<PotStateDto>>() {})
//
//        Assertions.assertThat(result).isNotNull
//        Assertions.assertThat(result.statusCode).isSameAs(HttpStatus.OK)
//        Assertions.assertThat(result.body).isNotNull
//        Assertions.assertThat(result.body!!.status).isSameAs(ResponseStatus.SUCCESS) //todo figure out about generic
//        Assertions.assertThat(result.body!!.payload!!.potName).isEqualTo(dto.potName)
//        Assertions.assertThat(result.body!!.payload!!.date).isEqualTo(dto.date)
//        Assertions.assertThat(result.body!!.payload!!.humidity).isEqualTo(dto.humidity)
//        Assertions.assertThat(result.body!!.payload!!.id).isGreaterThan(0)
//    }
//
//    @Test
//    fun saveAndFind() {
//        val dto1 = PotStateDto(
//            potName = "pot",
//            date = Date().time,
//            humidity = 1.0
//        )
//
//        val saved1 = restTemplate.exchange(
//            "/potstate/save",
//            HttpMethod.POST,
//            HttpEntity(dto1),
//            object : ParameterizedTypeReference<Response<PotStateDto>>() {})
//
//        Assertions.assertThat(saved1.statusCode).isSameAs(HttpStatus.OK)
//
//        val dto2 = PotStateDto(
//            potName = "pot",
//            date = Date().time,
//            humidity = 2.0
//        )
//
//        val saved2 = restTemplate.exchange(
//            "/potstate/save",
//            HttpMethod.POST,
//            HttpEntity(dto1),
//            object : ParameterizedTypeReference<Response<PotStateDto>>() {})
//
//        Assertions.assertThat(saved2.statusCode).isSameAs(HttpStatus.OK)
//
//        val builder = UriComponentsBuilder.fromPath("/potstate/list")
//            .queryParam("potName", dto1.potName)
//
//        val found = restTemplate.exchange(
//            builder.build().encode().toUri(),
//            HttpMethod.GET,
//            null,
//            object : ParameterizedTypeReference<Response<List<PotStateDto>>>() {}
//        )
//        Assertions.assertThat(found.statusCode).isSameAs(HttpStatus.OK)
//        Assertions.assertThat(found.body).isNotNull
//        Assertions.assertThat(found.body!!.status).isSameAs(ResponseStatus.SUCCESS) //todo figure out about generic
//        Assertions.assertThat(found.body!!.message).isEqualTo("message was handled successfully")
//        Assertions.assertThat(found.body!!.payload).isInstanceOf(ArrayList::class.java)
//        Assertions.assertThat(found.body!!.payload).hasSize(2)
//        Assertions.assertThat(found.body!!.payload).hasOnlyElementsOfType(PotStateDto::class.java)
//        //todo check elements
//    }
}