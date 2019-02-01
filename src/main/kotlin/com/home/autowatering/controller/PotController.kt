package com.home.autowatering.controller

import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.controller.converter.PotStateConverter
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.PotStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.exception.PotAlreadyExistException
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

    @GetMapping("/{pot}")
    fun get(@PathVariable(value = "pot") potId: Long): Response<PotDto> {
        val pot = potService.find(PotFilter(id = potId))
            .singleOrNull() ?: throw PotNotFoundException(potId)
        val state = potStateService.last(pot)
        return potConverter.response(pot, state)
    }

    @PostMapping("/create")
    fun create(@RequestBody request: PotDto): Response<PotDto> {
        var pot = potService.find(PotFilter(code = request.code)).singleOrNull()
        if (pot != null) {
            throw PotAlreadyExistException(code = request.code)
        }
        pot = potService.save(potConverter.fromDto(request))
        return potConverter.response(pot)
    }

    @PostMapping("/update")
    fun update(@RequestBody request: PotDto): Response<PotDto> {
        var pot = potService.find(PotFilter(code = request.code))
            .singleOrNull() ?: throw PotNotFoundException(code = request.code)
        pot = potService.merge(potConverter.fromDto(request), pot)
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