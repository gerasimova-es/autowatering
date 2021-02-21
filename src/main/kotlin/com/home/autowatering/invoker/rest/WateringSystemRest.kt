package com.home.autowatering.invoker.rest

import com.home.autowatering.invoker.WateringSystemInvoker
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository


@Repository
class WateringSystemRest : WateringSystemInvoker {

    companion object {

        const val REFRESH_SETTINGS = "/settings/change"
        const val GET_STATE = "/state/info"
    }

    @Value("\${autowatering.board.url}")
    var url: String? = null

    override fun refreshSettings() {
//        RestTemplate().exchange(
//            url + REFRESH_SETTINGS,
//            HttpMethod.POST,
//            HttpEntity(PotConverter.fromEntity(pot)),
//            object : ParameterizedTypeReference<PotDto>() {})
    }
}