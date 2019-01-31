package com.home.autowatering.model.database.converter

import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.database.JpaPotState
import java.util.function.Function

class JpaPotStateConverter : JpaConverter<JpaPotState, PotState>(
    Function {
        PotState(
            id = it.id,
            pot = JpaPotConverter().fromJpa(it.pot!!),
            date = it.date!!,
            humidity = it.humidity!!,
            watering = it.watering
        )
    },
    Function {
        JpaPotState(
            id = it.id,
            pot = JpaPotConverter().fromEntity(it.pot),
            date = it.date,
            humidity = it.humidity,
            watering = it.watering
        )
    }
)