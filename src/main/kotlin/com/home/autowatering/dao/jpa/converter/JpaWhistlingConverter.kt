package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaWhistling
import com.home.autowatering.model.settings.Whistling

object JpaWhistlingConverter : JpaConverter<JpaWhistling, Whistling>(
    {
        Whistling(
            it.id,
            it.enabled!!,
            it.duration!!,
            it.startTime!!,
            it.stopTime!!
        )
    },
    {
        JpaWhistling(
            it.id,
            it.enabled,
            it.duration,
            it.startTime,
            it.stopTime
        )
    }
)