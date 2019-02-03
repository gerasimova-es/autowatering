package com.home.autowatering.dao

import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.dao.interfaces.WateringSystemDao
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

    val converter = PotConverter()

    override fun refresh(pot: Pot) {
        val response = RestTemplate().exchange(
            url + REFRESH_SERVICE,
            HttpMethod.POST,
            HttpEntity(converter.fromEntity(pot)),
            object : ParameterizedTypeReference<Response<PotDto>>() {})
    }
}