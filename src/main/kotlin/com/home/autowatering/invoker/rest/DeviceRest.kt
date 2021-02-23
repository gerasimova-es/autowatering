package com.home.autowatering.invoker.rest

import com.home.autowatering.api.converter.DeviceSettingsDtoConverter
import com.home.autowatering.invoker.DeviceInvoker
import com.home.autowatering.invoker.converter.DeviceStateDtoConverter
import com.home.autowatering.invoker.dto.state.DeviceStateDto
import com.home.autowatering.model.history.DeviceState
import com.home.autowatering.model.settings.DeviceSettings
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate


@Repository
class DeviceRest : DeviceInvoker {

    companion object {
        const val REFRESH_SETTINGS = "/settings/change"
        const val GET_STATE = "/state/info"
        const val WATERING = "/watering/force"
    }

    @Value("\${autowatering.board.url}")
    var url: String? = null

    override fun getState(): DeviceState {
        val response = RestTemplate().exchange(
            url + GET_STATE,
            HttpMethod.POST,
            null,
            object : ParameterizedTypeReference<DeviceStateDto>() {}
        )
        if (response.statusCode != HttpStatus.OK) {
            throw RuntimeException("could not get state: " + response.statusCodeValue)
        }
        if (!response.hasBody()) {
            throw RuntimeException("body is empty" + response.statusCodeValue)
        }

        return DeviceStateDtoConverter.fromDto(response.body!!)
    }

    override fun refreshSettings(settings: DeviceSettings) {
        val response = RestTemplate().exchange(
            url + REFRESH_SETTINGS,
            HttpMethod.POST,
            HttpEntity(
                DeviceSettingsDtoConverter.fromEntity(settings)
            ),
            object : ParameterizedTypeReference<String>() {}
        )

        if (response.statusCode != HttpStatus.OK) {
            throw RuntimeException("could not refresh settings: " + response.statusCodeValue)
        }
    }

    override fun watering() {
        val response = RestTemplate().exchange(
            url + WATERING,
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<String>() {}
        )
        if (response.statusCode != HttpStatus.OK) {
            throw RuntimeException("could force watering: " + response.statusCodeValue)
        }
    }
}