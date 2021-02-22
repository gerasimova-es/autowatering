package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaLightingHistory
import com.home.autowatering.model.history.LightingHistory

object JpaLightingHistoryConverter : JpaConverter<JpaLightingHistory, LightingHistory>(
    {
        LightingHistory(
            it.status!!,
            it.checkDate!!
        )
    },
    {
        JpaLightingHistory(
            it.status,
            it.checkDate
        )
    }
)