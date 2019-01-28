package com.home.autowatering.controller

import com.home.autowatering.converter.PotConverter
import com.home.autowatering.converter.PotStateConverter
import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotFilter
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotService
import com.home.autowatering.service.interfaces.PotStateService
import org.apache.commons.lang.Validate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/pot")
class PotController(var potService: PotService, var potStateService: PotStateService) : AbstractController() {
    final val potConverter = PotConverter()
    final val potStateConverter = PotStateConverter()

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @GetMapping("/list")
    fun list(): Response<List<PotDto>> {
        val pots = potService.findAll()
        return potConverter.response(pots)
    }

    @GetMapping("/{pot}")
    fun get(@PathVariable(value = "pot") potId: Long): Response<PotDto> {
        val pot = Optional.ofNullable(potService.find(PotFilter(id = potId)))
            .orElseThrow { PotNotFoundException(potId) }
        return potConverter.response(pot)
    }

    @PostMapping("/save")
    fun save(@RequestBody request: PotDto): Response<PotDto> {
        val saved = potService.find(PotFilter(code = request.code))

        var pot =
            if (saved == null)
                potConverter.fromDto(request)
            else
                potService.merge(potConverter.fromDto(request), saved)
        pot = potService.save(pot)

        return potConverter.response(pot)
    }

    @GetMapping("/{pot}/history")
    fun states(
        @PathVariable(value = "pot") potId: Long,
        @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(pattern = DATE_FORMAT) dateFrom: Date?,
        @RequestParam(value = "dateTo", required = false) @DateTimeFormat(pattern = DATE_FORMAT) dateTo: Date?
    ): Response<List<PotStateDto>> {
        LOGGER.info("received pot history request")
        val pot = potService.find(PotFilter(id = potId))
        Validate.notNull(pot) //todo PotNotFoundException
        val states: List<PotState> = potStateService.find(PotStateFilter(pot!!, dateFrom, dateTo))
        LOGGER.info("found ${states.size} records")
        return potStateConverter.response(states)
    }


    @PostMapping("/state/save")
    fun saveState(@RequestBody request: PotStateDto): Response<PotStateDto> {
        LOGGER.info("received save pot state request = [$request]")
        var state = potStateConverter.fromDto(request)

        var pot = Optional.ofNullable(potService.find(PotFilter(code = state.pot.code)))
            .orElseThrow { PotNotFoundException(state.pot.code) }

        pot = potService.save(potService.mergeState(state, pot))
        state = potStateService.save(state) //todo in one transaction?
        LOGGER.info("state for pot = [$pot] saved successfully")
        return potStateConverter.response(state)
    }

}