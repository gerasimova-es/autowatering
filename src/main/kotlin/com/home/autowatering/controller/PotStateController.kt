package com.home.autowatering.controller

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.SarchPotStateResponse
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class PotStateController(var potStateService: PotStateService) {

    @GetMapping("/potstate/find")
    fun find(
        @RequestParam(value = "potName") potName: String,
        @RequestParam(value = "dateFrom", required = false) dateFrom: Date,
        @RequestParam(value = "dateTo", required = false) dateTo: Date
    ): SarchPotStateResponse {
        try {
            val states: List<PotState> = potStateService.find(
                PotStateFilter.withPot(Pot(potName))
                    .from(dateFrom)
                    .to(dateTo)
                    .build()
            )
            return response(states)
        } catch (exc: Exception) {
            return response(exc)
        }
    }

    fun response(exc: Exception) =
    //SarchPotStateResponse(ResponseStatus.ERROR, exc.message) //todo
        SarchPotStateResponse(arrayListOf()) //todo


    fun response(states: List<PotState>): SarchPotStateResponse =
        SarchPotStateResponse(states.map { state ->
            PotStateDto(
                state.id!!,
                state.pot.name!!,
                state.date,
                state.humidity
            )
        })

}