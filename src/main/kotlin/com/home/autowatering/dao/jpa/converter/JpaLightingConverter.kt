package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaLighting
import com.home.autowatering.model.settings.Lighting

object JpaLightingConverter : JpaConverter<JpaLighting, Lighting>(
    {
        Lighting(
            it.enabled!!,
            it.startTime!!,
            it.stopTime!!
        )
    },
    {
        JpaLighting(
            it.enabled,
            it.startTime,
            it.stopTime
        )
    }
)