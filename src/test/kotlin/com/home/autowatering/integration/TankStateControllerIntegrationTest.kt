package com.home.autowatering.integration

import jdk.nashorn.internal.ir.annotations.Ignore


@Ignore
class TankStateControllerIntegrationTest {

//    lateinit var restTemplate: TestRestTemplate
//
//    @Test
//    fun save() {
//        val dto = TankStateDto(
//            name = "tank",
//            volume = 2.178,
//            filled = 0.0,
//            date = Date()
//        )
//
//        val result = restTemplate.exchange(
//            "/tank/state/save",
//            HttpMethod.POST,
//            HttpEntity(dto),
//            object : ParameterizedTypeReference<Response<TankStateDto>>() {})
//
//        Assertions.assertThat(result).isNotNull
//        Assertions.assertThat(result.statusCode).isSameAs(HttpStatus.OK)
//        Assertions.assertThat(result.body).isNotNull
//        Assertions.assertThat(result.body!!.status).isSameAs(ResponseStatus.SUCCESS)
//        Assertions.assertThat(result.body!!.payload!!.id).isGreaterThan(0)
//        Assertions.assertThat(result.body!!.payload!!.name).isEqualTo(dto.name)
//        Assertions.assertThat(result.body!!.payload!!.volume).isEqualTo(dto.volume)
//    }
}