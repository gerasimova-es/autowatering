package com.home.autowatering.controller

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.dto.response.SearchPotStateResponse
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.impl.WateringStateServiceImpl
import com.home.autowatering.service.interfaces.PotStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class PotStateController(var potStateService: PotStateService) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(WateringStateServiceImpl::class.java)
    }

    @GetMapping("/potstate/list")
    fun find(
        @RequestParam(value = "potName") potName: String,
        @RequestParam(value = "dateFrom", required = false) dateFrom: Date?,
        @RequestParam(value = "dateTo", required = false) dateTo: Date?
    ): SearchPotStateResponse {
        try {
            val states: List<PotState> = potStateService.find(
                PotStateFilter.withPot(Pot(potName))
                    .from(dateFrom)
                    .to(dateTo)
                    .build()
            )
            return response(states)
        } catch (exc: Exception) {
            logger.error("Ошибка выполнения запроса", exc)
            return response(exc)
        }
    }

    fun response(exc: Exception) =
        SearchPotStateResponse(ResponseStatus.ERROR, if (exc.message == null) exc.javaClass.name else exc.message!!)


    fun response(states: List<PotState>): SearchPotStateResponse =
        SearchPotStateResponse(states.map { state ->
            PotStateDto(
                state.id!!,
                state.pot.name!!,
                state.date,
                state.humidity
            )
        })

}