package com.home.autowatering.invoker.rest

import com.home.autowatering.invoker.WateringSystemInvoker
import com.home.autowatering.invoker.state.DeviceStateDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate


@Repository
class WateringSystemRest : WateringSystemInvoker {

    companion object {

        const val REFRESH_SETTINGS = "/settings/change"
        const val GET_STATE = "/state/info"
    }

    @Value("\${autowatering.board.url}")
    var url: String? = null

    override fun getState(): DeviceStateDto {
        val response = RestTemplate().exchange(
            url + GET_STATE,
            HttpMethod.POST,
            null,
            object : ParameterizedTypeReference<DeviceStateDto>() {}
        )
        if (response.statusCode == HttpStatus.OK) {
            if(response.hasBody()){
                return response.body!!
            } else {
                throw RuntimeException("body is empty" + response.statusCodeValue)
            }
        }
        throw RuntimeException("could not get state: " + response.statusCodeValue)
    }

//    override fun refreshSettings() {
//        RestTemplate().exchange(
//            url + REFRESH_SETTINGS,
//            HttpMethod.POST,
//            HttpEntity(PotConverter.fromEntity(pot)),
//            object : ParameterizedTypeReference<PotDto>() {})
//    }
}