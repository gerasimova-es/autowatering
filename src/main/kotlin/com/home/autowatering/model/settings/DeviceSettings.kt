package com.home.autowatering.model.settings

data class DeviceSettings(
    var lighting: Lighting? = null,
    var watering: Watering? = null,
    var whistling: Whistling? = null,
    var vaporizer: Vaporizer? = null
)