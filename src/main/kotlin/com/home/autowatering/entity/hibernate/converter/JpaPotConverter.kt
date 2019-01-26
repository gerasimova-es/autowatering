package com.home.autowatering.entity.hibernate.converter

import com.home.autowatering.entity.hibernate.JpaPot
import com.home.autowatering.entity.hibernate.JpaPotState
import com.home.autowatering.model.Pot
import java.util.function.Function

class JpaPotConverter : JpaConverter<JpaPot, Pot>(
    Function {
        Pot(
            id = it.id,
            name = it.name!!,
            description = it.description
        )
    },
    Function {
        JpaPot(
            id = it.id,
            name = it.name,
            description = it.description
        )
    }

) {
    fun fromJpa(pot: JpaPot, state: JpaPotState?): Pot {
        val potPojo = super.fromJpa(pot)
        if (state != null) {
            potPojo.state = JpaPotStateConverter().fromJpa(state)
        }
        return potPojo
    }

    fun map(source: Pot, target: JpaPot): JpaPot {
        target.name = source.name
        target.description = source.description
        return target
    }
}