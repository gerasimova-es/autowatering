package com.home.autowatering.converter

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import org.apache.commons.lang.Validate
import java.util.*

class PotStateConverter() : RequestConverter {
    var request: PotStateDto? = null
    var dateFrom: Date? = null
    var dateTo: Date? = null

    constructor(request: PotStateDto) : this() {
        this.request = request
    }

    constructor(potName: String, dateFrom: Date?, dateTo: Date?) : this() {
        this.request = PotStateDto(potName)
        this.dateFrom = dateFrom
        this.dateTo = dateTo
    }

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
            Pot(request!!.potName),
            request!!.date!!,
            request!!.humidity!!
        )
    }

    fun getFilter(): PotStateFilter =
        PotStateFilter.withPot(Pot(request!!.potName))
            .from(dateFrom)
            .to(dateTo)
            .build()


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