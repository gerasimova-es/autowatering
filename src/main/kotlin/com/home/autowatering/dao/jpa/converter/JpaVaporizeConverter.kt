package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaVaporizer
import com.home.autowatering.model.settings.Vaporizer

object JpaVaporizeConverter : JpaConverter<JpaVaporizer, Vaporizer>(
    {
        Vaporizer(
            it.enabled!!,
            it.minHumidity!!,
            it.checkInterval!!
        )
    },
    {
        JpaVaporizer(
            it.enabled,
            it.minHumidity,
            it.checkInterval
        )
    }
)