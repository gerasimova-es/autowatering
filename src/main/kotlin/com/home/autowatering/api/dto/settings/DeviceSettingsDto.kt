package com.home.autowatering.api.dto.settings

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DeviceSettingsDto(
    var watering: WateringSettingsDto,
    var vaporize: VaporizeSettingsDto,
    var lighting: LightingSettingsDto,
    var whistling: WhistlingSettingsDto
)