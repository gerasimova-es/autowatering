package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaWhistlingSettings
import com.home.autowatering.model.WhistlingSettings

object JpaWhistlingSettingsConverter : JpaConverter<JpaWhistlingSettings, WhistlingSettings>(
    {
        WhistlingSettings(
            it.enabled!!,
            it.duration!!,
            it.startTime!!,
            it.stopTime!!
        )
    },
    {
        JpaWhistlingSettings(
            null,
            it.enabled,
            it.duration,
            it.startTime,
            it.stopTime
        )
    }
)