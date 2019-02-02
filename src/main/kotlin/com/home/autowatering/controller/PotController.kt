package com.home.autowatering.controller

import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.controller.converter.PotStateConverter
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.PotStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.filter.PotFilter
import com.home.autowatering.model.business.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotService
import com.home.autowatering.service.interfaces.PotStateService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/pot")
class PotController(var potService: PotService, var potStateService: PotStateService) : AbstractController() {
    private final val potConverter = PotConverter()
    private final val potStateConverter = PotStateConverter()

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @GetMapping("/list")
    fun list(): Response<List<PotDto>> {
        val pots = potService.findAll()
        //todo set humidity to response
        return potConverter.response(pots)
    }

    @GetMapping("/info")
    fun info(@RequestParam(value = "code") potCode: String): Response<PotDto> {
        val pot = potService.find(PotFilter(code = potCode))
            .singleOrNull() ?: throw PotNotFoundException(potCode)
        val state = potStateService.last(pot)
        return potConverter.response(pot, state)
    }

    @GetMapping("/{pot}")
    fun get(@PathVariable(value = "pot") potId: Long): Response<PotDto> {
        val pot = potService.find(PotFilter(id = potId))
            .singleOrNull() ?: throw PotNotFoundException(potId)
        return potConverter.response(pot)
    }

    @PostMapping("/save")
    fun save(@RequestBody request: PotDto): Response<PotDto> {
        val saved = potService.find(PotFilter(id = request.id, code = request.code)).singleOrNull()
        var pot = if (saved == null) potConverter.fromDto(request)
        else potService.merge(potConverter.fromDto(request), saved)
        pot = potService.save(pot)
        return potConverter.response(pot)
    }

    @GetMapping("/{pot}/state/list")
    fun states(
        @PathVariable(value = "pot") potId: Long,
        @RequestParam(value = "dateFrom", required = false) @DateTimeFormat(pattern = DATE_FORMAT) dateFrom: Date?,
        @RequestParam(value = "dateTo", required = false) @DateTimeFormat(pattern = DATE_FORMAT) dateTo: Date?
    ): Response<List<PotStateDto>> {
        val pot = potService.find(PotFilter(id = potId))
            .singleOrNull() ?: throw PotNotFoundException(potId)
        val states = potStateService.find(PotStateFilter(pot, dateFrom, dateTo))
        return potStateConverter.response(states)
    }

    @PostMapping("/state/save")
    fun saveState(@RequestBody request: PotStateDto): Response<PotStateDto> {
        var state = potStateConverter.fromDto(request)
        state = potStateService.save(state)
        return potStateConverter.response(state)
    }

}