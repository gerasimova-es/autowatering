package com.home.autowatering.api.rest

import com.home.autowatering.api.converter.PotConverter
import com.home.autowatering.api.converter.PotStateConverter
import com.home.autowatering.api.dto.PotDto
import com.home.autowatering.api.dto.PotStateDto
import com.home.autowatering.api.dto.response.Response
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.filter.PotFilter
import com.home.autowatering.model.business.filter.PotStateFilter
import com.home.autowatering.service.PotService
import com.home.autowatering.service.PotStateService
import com.home.autowatering.service.WateringSystemService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/pot")
class PotController(
    var potService: PotService, var potStateService: PotStateService,
    var wateringSystemService: WateringSystemService
) : AbstractController() {

    @GetMapping("/list")
    fun list(): Response<List<PotDto>> {
        val pots = potService.findAll()
            .map { pot ->
                pot.apply {
                    this.humidity = potStateService.last(this)?.humidity
                }
            }
        return PotConverter.response(pots)
    }

    @GetMapping("/info")
    fun info(@RequestParam(value = "code") potCode: String): Response<PotDto> {
        val pot = potService.find(PotFilter(code = potCode))
            .singleOrNull() ?: throw PotNotFoundException(potCode)
        val state = potStateService.last(pot)
        return PotConverter.response(pot, state)
    }

    @PostMapping("/save")
    fun save(@RequestBody request: PotDto): Response<PotDto> {
        val saved = potService.find(PotFilter(id = request.id, code = request.code)).singleOrNull()
        var pot = if (saved == null) PotConverter.fromDto(request)
        else potService.merge(PotConverter.fromDto(request), saved)
        pot = potService.save(pot)
        wateringSystemService.refresh(pot)
        return PotConverter.response(pot)
    }

    @GetMapping("/statistic/{pot}")
    fun states(
        @PathVariable(value = "pot") potCode: String,
        @RequestParam(
            value = "dateFrom",
            required = false
        ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) dateFrom: Date?,
        @RequestParam(
            value = "dateTo",
            required = false
        ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) dateTo: Date?
    ): Response<List<PotStateDto>> {
        val pot = potService.find(PotFilter(code = potCode))
            .singleOrNull() ?: throw PotNotFoundException(potCode)
        val states = potStateService.find(PotStateFilter(pot, dateFrom, dateTo))
        return PotStateConverter.response(states)
    }

}