package com.home.autowatering.api.dto.settings

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DeviceSettingsDto(
    var watering: WateringDto? = null,
    var vaporize: VaporizeDto? = null,
    var lighting: LightingDto? = null,
    var whistling: WhistlingDto? = null
)