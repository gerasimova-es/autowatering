package com.home.autowatering.converter

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import java.util.*

class PotStateFilterConverter(val potName: String, var dateFrom: Date? = null, var dateTo: Date? = null) {
    fun getFilter(): PotStateFilter =
        PotStateFilter.withPot(Pot(name = potName))
            .from(dateFrom)
            .to(dateTo)
            .build()

    fun response(states: List<PotState>): Response<List<PotStateDto>> =
        Response(states.map { state ->
            convert(state)
        })

    private fun convert(state: PotState): PotStateDto =
        PotStateDto(
            state.id!!,
            state.pot.name!!,
            state.date,
            state.humidity
        )
}