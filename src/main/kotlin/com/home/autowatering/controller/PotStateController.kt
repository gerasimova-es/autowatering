package com.home.autowatering.controller

import com.home.autowatering.converter.PotStateConverter
import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.PotState
import com.home.autowatering.service.interfaces.PotStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/potstate")
class PotStateController(var potStateService: PotStateService) : AbstractController() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(PotStateController::class.java)
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: PotStateDto): Response<PotStateDto> {
        logger.info("received saving state request. Executing...")

        val converter = PotStateConverter(request)
        converter.validate()
        val state = potStateService.save(converter.getState()!!)

        logger.info("pot state [$state] saved successfully")
        return converter.response(state)
    }

    @GetMapping("/list")
    fun find(
        @RequestParam(value = "potName") potName: String,
        @RequestParam(value = "dateFrom", required = false) dateFrom: Date?,
        @RequestParam(value = "dateTo", required = false) dateTo: Date?
    ): Response<List<PotStateDto>> {
        logger.info("received search pot state request. Search executing...")

        val converter = PotStateConverter(potName, dateFrom, dateTo)
        converter.validate()
        val filter = converter.getFilter()
        val states: List<PotState> = potStateService.find(filter)

        logger.info("found ${states.size} records with filter [$filter]")

        return converter.response(states)
    }
}