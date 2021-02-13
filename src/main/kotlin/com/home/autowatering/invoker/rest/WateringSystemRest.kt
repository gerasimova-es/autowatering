package com.home.autowatering.invoker.rest

import com.home.autowatering.api.converter.PotConverter
import com.home.autowatering.api.dto.PotDto
import com.home.autowatering.api.dto.response.Response
import com.home.autowatering.dao.WateringSystemDao
import com.home.autowatering.model.business.Pot
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate


@Repository
class WateringSystemRest : WateringSystemDao {
    companion object {
        const val REFRESH_SERVICE = "/pot/settings/save"
    }

    @Value("\${watering.url}")
    var url: String? = null

    override fun refresh(pot: Pot) {
        RestTemplate().exchange(
            url + REFRESH_SERVICE,
            HttpMethod.POST,
            HttpEntity(PotConverter.fromEntity(pot)),
            object : ParameterizedTypeReference<Response<PotDto>>() {})
    }
}