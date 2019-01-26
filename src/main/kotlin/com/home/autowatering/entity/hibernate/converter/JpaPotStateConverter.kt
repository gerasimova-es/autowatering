package com.home.autowatering.entity.hibernate.converter

import com.home.autowatering.entity.hibernate.JpaPotState
import com.home.autowatering.model.PotState
import java.util.*
import java.util.function.Function

class JpaPotStateConverter : JpaConverter<JpaPotState, PotState>(
    Function {
        PotState(
            id = it.id,
            date = Date(it.date!!.time),
            humidity = it.humidity!!
        )
    },
    Function {
        JpaPotState(
            id = it.id,
            date = java.sql.Date(it.date.time),
            humidity = it.humidity
        )
    }
)