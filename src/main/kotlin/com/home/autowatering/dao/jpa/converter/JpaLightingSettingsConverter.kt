package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaLightingSettings
import com.home.autowatering.model.LightingSettings

object JpaLightingSettingsConverter : JpaConverter<JpaLightingSettings, LightingSettings>(
    {
        LightingSettings(
            it.enabled!!,
            it.startTime!!,
            it.stopTime!!
        )
    },
    {
        JpaLightingSettings(
            null,
            it.enabled,
            it.startTime,
            it.stopTime
        )
    }
)