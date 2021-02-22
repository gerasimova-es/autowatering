package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.settings.VaporizeSettingsDto
import com.home.autowatering.model.settings.Vaporizer

object VaporizeSettingsConverter : RequestConverter<VaporizeSettingsDto, Vaporizer>(
    {
        Vaporizer(
            it.enabled,
            it.minHumidity,
            it.interval
        )
    },
    {
        VaporizeSettingsDto(
            it.enabled,
            it.minHumidity,
            it.checkInterval
        )
    }
)