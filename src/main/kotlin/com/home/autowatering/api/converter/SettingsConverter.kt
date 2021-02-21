package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.settings.DeviceSettingsDto
import com.home.autowatering.model.LightingSettings
import com.home.autowatering.model.VaporizeSettings
import com.home.autowatering.model.WateringSettings
import com.home.autowatering.model.WhistlingSettings

object SettingsConverter {
    fun convert(
        lightingSettings: LightingSettings,
        vaporizeSettings: VaporizeSettings,
        whistlingSettings: WhistlingSettings,
        wateringSettings: WateringSettings
    ): DeviceSettingsDto {
        return DeviceSettingsDto(
            WateringSettingsConverter.fromEntity(wateringSettings),
            VaporizeSettingsConverter.fromEntity(vaporizeSettings),
            LightingSettingsConverter.fromEntity(lightingSettings),
            WhistlingSettingsConverter.fromEntity(whistlingSettings)
        )
    }
}