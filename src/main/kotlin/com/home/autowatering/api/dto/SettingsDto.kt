package com.home.autowatering.api.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SettingsDto(
    var watering: WateringSettingsDto,
    var vaporize: VaporizeSettingsDto,
    var lighting: LightingSettingsDto,
    var whistling: WhistlingSettingsDto
)