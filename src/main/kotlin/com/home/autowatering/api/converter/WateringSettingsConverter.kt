package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.settings.WateringSettingsDto
import com.home.autowatering.model.settings.Watering

object WateringSettingsConverter : RequestConverter<WateringSettingsDto, Watering>(
    {
        Watering(
            it.enabled,
            it.minHumidity,
            it.interval,
            it.duration
        )
    },
    {
        WateringSettingsDto(
            it.enabled,
            it.minHumidity,
            it.checkInterval,
            it.duration
        )
    }
)