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
import com.home.autowatering.service.interfaces.WateringSystemService
import java.util.*

//@RestController
//@RequestMapping("/pot")
class PotController(
    var potService: PotService, var potStateService: PotStateService,
    var wateringSystemService: WateringSystemService
) : AbstractController() {

    //    @GetMapping("/list")
    fun list(): Response<List<PotDto>> {
        val pots = potService.findAll()
            .map { pot ->
                pot.apply {
                    this.humidity = potStateService.last(this)?.humidity
                }
            }
        return PotConverter.response(pots)
    }

    //    @GetMapping("/info")
    fun info(
//    @RequestParam(value = "code")
        potCode: String
    ): Response<PotDto> {
        val pot = potService.find(PotFilter(code = potCode))
            .singleOrNull() ?: throw PotNotFoundException(potCode)
        val state = potStateService.last(pot)
        return PotConverter.response(pot, state)
    }

    //    @PostMapping("/save")
    fun save(
//    @RequestBody
        request: PotDto
    ): Response<PotDto> {
        val saved = potService.find(PotFilter(id = request.id, code = request.code)).singleOrNull()
        var pot = if (saved == null) PotConverter.fromDto(request)
        else potService.merge(PotConverter.fromDto(request), saved)
        pot = potService.save(pot)
        wateringSystemService.refresh(pot)
        return PotConverter.response(pot)
    }

    //    @GetMapping("/statistic/{pot}")
    fun states(
//        @PathVariable(value = "pot")
        potCode: String,
//        @RequestParam(
//            value = "dateFrom",
//            required = false
//        ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        dateFrom: Date?,
//        @RequestParam(
//            value = "dateTo",
//            required = false
//        ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        dateTo: Date?
    ): Response<List<PotStateDto>> {
        val pot = potService.find(PotFilter(code = potCode))
            .singleOrNull() ?: throw PotNotFoundException(potCode)
        val states = potStateService.find(PotStateFilter(pot, dateFrom, dateTo))
        return PotStateConverter.response(states)
    }

    //    @PostMapping("/state/save")
    fun saveState(
//    @RequestBody
        request: PotStateDto
    ): Response<PotStateDto> {
        var state = PotStateConverter.fromDto(request)
        state = potStateService.save(state)
        return PotStateConverter.response(state)
    }

}