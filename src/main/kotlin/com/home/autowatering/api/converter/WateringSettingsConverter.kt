package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.settings.WateringSettingsDto
import com.home.autowatering.model.WateringSettings

object WateringSettingsConverter : RequestConverter<WateringSettingsDto, WateringSettings>(
    {
        WateringSettings(
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
            it.interval,
            it.duration
        )
    }
)