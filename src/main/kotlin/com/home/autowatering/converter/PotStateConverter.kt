package com.home.autowatering.converter

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import org.apache.commons.lang.Validate

class PotStateConverter(var request: PotStateDto? = null) : RequestConverter {

    override fun validate() {
        if (request == null) {
            return
        }
        Validate.notNull(request!!.potName)
        if (request!!.humidity != null) {
            Validate.notNull(request!!.date)
        }
    }

    fun getState(): PotState? {
        if (request == null) {
            return null
        }
        return PotState(
            pot = Pot(name = request!!.potName),
            date = request!!.date!!,
            humidity = request!!.humidity!!
        )
    }

    fun response(state: PotState): Response<PotStateDto> {
        return Response(convert(state))
    }

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