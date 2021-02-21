package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaWateringSettings
import com.home.autowatering.model.WateringSettings

object JpaWateringSettingsConverter : JpaConverter<JpaWateringSettings, WateringSettings>(
    {
        WateringSettings(
            it.enabled!!,
            it.minHumidity!!,
            it.interval!!,
            it.duration!!
        )
    },
    {
        JpaWateringSettings(
            null,
            it.enabled,
            it.minHumidity,
            it.interval,
            it.duration
        )
    }
)