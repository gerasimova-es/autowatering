package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.VaporizeSettingsDto
import com.home.autowatering.model.VaporizeSettings

object VaporizeSettingsConverter : RequestConverter<VaporizeSettingsDto, VaporizeSettings>(
    {
        VaporizeSettings(
            it.enabled,
            it.minHumidity,
            it.interval
        )
    },
    {
        VaporizeSettingsDto(
            it.enabled,
            it.minHumidity,
            it.interval
        )
    }
)