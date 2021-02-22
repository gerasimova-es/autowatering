package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaAirCondition
import com.home.autowatering.model.history.AirCondition

object JpaAirConditionConverter : JpaConverter<JpaAirCondition, AirCondition>(
    {
        AirCondition(
            it.humidity!!,
            it.temperature!!,
            it.checkDate!!
        )
    },
    {
        JpaAirCondition(
            it.humidity,
            it.temperature,
            it.checkDate
        )
    }
)