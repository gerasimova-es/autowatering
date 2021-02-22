package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.settings.DeviceSettingsDto
import com.home.autowatering.model.settings.Lighting
import com.home.autowatering.model.settings.Vaporizer
import com.home.autowatering.model.settings.Watering
import com.home.autowatering.model.settings.Whistling

object SettingsConverter {

    fun convert(
        lighting: Lighting,
        vaporizer: Vaporizer,
        whistling: Whistling,
        watering: Watering
    ): DeviceSettingsDto {
        return DeviceSettingsDto(
            WateringSettingsConverter.fromEntity(watering),
            VaporizeSettingsConverter.fromEntity(vaporizer),
            LightingSettingsConverter.fromEntity(lighting),
            WhistlingSettingsConverter.fromEntity(whistling)
        )
    }
}