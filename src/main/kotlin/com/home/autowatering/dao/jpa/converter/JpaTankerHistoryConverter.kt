package com.home.autowatering.dao.jpa.converter

import com.home.autowatering.dao.jpa.entity.JpaTankerHistory
import com.home.autowatering.model.history.TankerHistory

object JpaTankerHistoryConverter : JpaConverter<JpaTankerHistory, TankerHistory>(
    {
        TankerHistory(
            it.full!!,
            it.checkDate!!
        )
    },
    {
        JpaTankerHistory(
            it.full,
            it.checkDate
        )
    }
)