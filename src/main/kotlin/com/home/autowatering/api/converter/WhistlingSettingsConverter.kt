package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.settings.WhistlingSettingsDto
import com.home.autowatering.model.settings.Whistling
import java.time.LocalTime

object WhistlingSettingsConverter : RequestConverter<WhistlingSettingsDto, Whistling>(
    {
        Whistling(
            it.enabled,
            it.duration,
            LocalTime.of(it.startHour, it.startMinute),
            LocalTime.of(it.stopHour, it.stopMinute)
        )
    },
    {
        WhistlingSettingsDto(
            it.enabled,
            it.duration,
            it.startTime.hour,
            it.startTime.minute,
            it.stopTime.hour,
            it.stopTime.minute
        )
    }
)