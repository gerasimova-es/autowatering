package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.settings.LightingSettingsDto
import com.home.autowatering.model.settings.Lighting
import java.time.LocalTime

object LightingSettingsConverter : RequestConverter<LightingSettingsDto, Lighting>(
    {
        Lighting(
            it.enabled,
            LocalTime.of(it.startHour, it.startMinute),
            LocalTime.of(it.stopHour, it.stopMinute)
        )
    },
    {
        LightingSettingsDto(
            it.enabled,
            it.startTime.hour,
            it.startTime.minute,
            it.stopTime.hour,
            it.stopTime.minute
        )
    }
)