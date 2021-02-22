package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaGroundCondition
import com.home.autowatering.model.history.GroundCondition

object JpaGroundConditionConverter : JpaConverter<JpaGroundCondition, GroundCondition>(
    {
        GroundCondition(
            it.humidity!!,
            it.checkDate!!
        )
    },
    {
        JpaGroundCondition(
            it.humidity,
            it.checkDate
        )
    }
)