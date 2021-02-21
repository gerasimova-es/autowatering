package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaVaporizeSettings
import com.home.autowatering.model.VaporizeSettings

object JpaVaporizeSettingsConverter : JpaConverter<JpaVaporizeSettings, VaporizeSettings>(
    {
        VaporizeSettings(
            it.enabled!!,
            it.minHumidity!!,
            it.interval!!
        )
    },
    {
        JpaVaporizeSettings(
            null,
            it.enabled,
            it.minHumidity,
            it.interval
        )
    }
)