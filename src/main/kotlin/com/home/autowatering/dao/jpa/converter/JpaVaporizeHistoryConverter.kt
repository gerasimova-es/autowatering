package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaVaporizerHistory
import com.home.autowatering.model.history.VaporizerHistory

object JpaVaporizeHistoryConverter : JpaConverter<JpaVaporizerHistory, VaporizerHistory>(
    {
        VaporizerHistory(
            it.status!!,
            it.checkDate!!
        )
    },
    {
        JpaVaporizerHistory(
            it.status,
            it.checkDate
        )
    }
)