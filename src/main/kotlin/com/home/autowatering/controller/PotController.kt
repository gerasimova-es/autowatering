package com.home.autowatering.controller

import com.home.autowatering.converter.PotConverter
import com.home.autowatering.converter.PotStateConverter
import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotFilter
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotService
import com.home.autowatering.service.interfaces.PotStateService
import org.apache.commons.lang.Validate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/pot")
class PotController(var potService: PotService, var potStateService: PotStateService) : AbstractController() {
    val potConverter = PotConverter()
    val potStateConverter = PotStateConverter()

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(PotController::class.java)
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @GetMapping("/list")
    fun list(): Response<List<PotDto>> {
        LOGGER.info("received pot list request. Search executing...")
        val pots = potService.findAll()
        LOGGER.info("found ${pots.size} pots")
        return potConverter.response(pots)
    }

    @GetMapping("/{pot}")
    fun get(@PathVariable(value = "pot") potId: Long): Response<PotDto> {
        LOGGER.info("received search pot by potCode request. Search executing...")
        val pot = potService.find(PotFilter(id = potId))
        Validate.notNull(pot) //todo PotNotFoundException
        LOGGER.info("found pot [$pot] ")
        return potConverter.response(pot!!)
    }

    @PostMapping("/save")
    fun save(@RequestBody request: PotDto): Response<PotDto> {
        LOGGER.info("received saving pot request. Executing...")
        val saved = potService.find(PotFilter(code = request.code))
        var pot = if (saved == null) potConverter.fromDto(request)
        else potService.merge(potConverter.fromDto(request), saved)
        pot = potService.save(pot)
        LOGGER.info("pot [$pot] saved successfully")
        return potConverter.response(pot)
    }

    @GetMapping("/{pot}/history")
    fun states(
        @PathVariable(value = "pot") potId: Long,
        @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(pattern = DATE_FORMAT) dateFrom: Date?,
        @RequestParam(value = "dateTo", required = false) @DateTimeFormat(pattern = DATE_FORMAT) dateTo: Date?
    ): Response<List<PotStateDto>> {
        LOGGER.info("received search pot state request. Search executing...")
        val pot = potService.find(PotFilter(id = potId))
        Validate.notNull(pot) //todo PotNotFoundException
        val states: List<PotState> = potStateService.find(PotStateFilter(pot!!, dateFrom, dateTo))
        LOGGER.info("found ${states.size} records")
        return potStateConverter.response(states)
    }


    @PostMapping("/state/save")
    fun saveState(@RequestBody request: PotStateDto): Response<PotStateDto> {
        LOGGER.info("received saving pot request. Executing...")
        var pot = potService.find(PotFilter(code = request.potCode))
        Validate.notNull(pot) //todo PotNotFoundException
        var state = potStateConverter.fromDto(request)
        pot = potService.mergeState(state, pot!!)
        potService.save(pot)
        state = potStateService.save(state)
        LOGGER.info("state for pot = [$pot] saved successfully")
        return potStateConverter.response(state)
    }

}