package com.home.autowatering.dao

import com.home.autowatering.controller.AbstractController
import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.model.business.Pot
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate


@Repository
class WateringSystemRest : WateringSystemDao {
    val converter = PotConverter()

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AbstractController::class.java)
    }

    //todo переписать
    override fun saveSetting(pot: Pot) {
        val url = "http://192.168.1.34:80/pot/settings/save"
        val restTemplate = RestTemplate()

        val response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            HttpEntity(converter.fromEntity(pot)),
            object : ParameterizedTypeReference<Response<PotDto>>() {})
        LOGGER.info("response = $response")
    }
}