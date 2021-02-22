package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaWatering
import com.home.autowatering.model.settings.Watering

object JpaWateringConverter : JpaConverter<JpaWatering, Watering>(
    {
        Watering(
            it.enabled!!,
            it.minHumidity!!,
            it.checkInterval!!,
            it.duration!!
        )
    },
    {
        JpaWatering(
            it.enabled,
            it.minHumidity,
            it.checkInterval,
            it.duration
        )
    }
)