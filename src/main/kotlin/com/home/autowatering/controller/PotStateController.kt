package com.home.autowatering.controller

import com.home.autowatering.converter.PotStateConverter
import com.home.autowatering.converter.PotStateFilterConverter
import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.PotStateFilterDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.PotState
import com.home.autowatering.service.interfaces.PotStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/potstate")
class PotStateController(var potStateService: PotStateService) : AbstractController() {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(PotStateController::class.java)
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @PostMapping("/save")
    fun save(@RequestBody request: PotStateDto): Response<PotStateDto> {
        LOGGER.info("received saving state request. Executing...")

        val converter = PotStateConverter()
        val state = potStateService.save(converter.fromDto(request))

        LOGGER.info("pot state [$state] saved successfully")
        return converter.response(state)
    }

    @GetMapping("/list")
    fun find(
        @RequestParam(value = "potName") potName: String,
        @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(pattern = DATE_FORMAT) dateFrom: Date?,
        @RequestParam(value = "dateTo", required = false) @DateTimeFormat(pattern = DATE_FORMAT) dateTo: Date?
    ): Response<List<PotStateDto>> {
        LOGGER.info("received search pot state request. Search executing...")

        val filter = PotStateFilterConverter().fromDto(PotStateFilterDto(potName, dateFrom, dateTo))
        val states: List<PotState> = potStateService.find(filter)

        LOGGER.info("found ${states.size} records with getFilter [$filter]")

        return PotStateConverter().response(states)
    }
}