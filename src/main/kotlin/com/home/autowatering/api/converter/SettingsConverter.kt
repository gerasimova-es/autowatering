package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.SettingsDto
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
    ): SettingsDto {
        return SettingsDto(
            WateringSettingsConverter.fromEntity(wateringSettings),
            VaporizeSettingsConverter.fromEntity(vaporizeSettings),
            LightingSettingsConverter.fromEntity(lightingSettings),
            WhistlingSettingsConverter.fromEntity(whistlingSettings)
        )
    }
}