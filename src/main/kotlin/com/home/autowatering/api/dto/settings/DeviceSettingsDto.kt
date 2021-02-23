package com.home.autowatering.api.dto.settings

class DeviceSettingsDto(
    var watering: WateringDto? = null,
    var vaporize: VaporizeDto? = null,
    var lighting: LightingDto? = null,
    var whistling: WhistlingDto? = null
)