package com.home.autowatering.invoker.rest

import com.home.autowatering.api.converter.LightingSettingsConverter
import com.home.autowatering.api.converter.VaporizeSettingsConverter
import com.home.autowatering.api.converter.WateringSettingsConverter
import com.home.autowatering.api.converter.WhistlingSettingsConverter
import com.home.autowatering.api.dto.settings.DeviceSettingsDto
import com.home.autowatering.invoker.DeviceInvoker
import com.home.autowatering.invoker.converter.DeviceStateConverter
import com.home.autowatering.invoker.dto.state.DeviceStateDto
import com.home.autowatering.model.history.DeviceState
import com.home.autowatering.model.settings.Lighting
import com.home.autowatering.model.settings.Vaporizer
import com.home.autowatering.model.settings.Watering
import com.home.autowatering.model.settings.Whistling
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

        return DeviceStateConverter.fromDto(response.body!!)
    }

    override fun refreshSettings(
        lighting: Lighting,
        watering: Watering,
        whistling: Whistling,
        vaporizer: Vaporizer
    ) {
        val response = RestTemplate().exchange(
            url + REFRESH_SETTINGS,
            HttpMethod.POST,
            HttpEntity(
                DeviceSettingsDto(
                    WateringSettingsConverter.fromEntity(watering),
                    VaporizeSettingsConverter.fromEntity(vaporizer),
                    LightingSettingsConverter.fromEntity(lighting),
                    WhistlingSettingsConverter.fromEntity(whistling)
                )
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