package com.home.autowatering.controller

import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.controller.converter.PotStateConverter
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.PotStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.filter.PotFilter
import com.home.autowatering.model.business.filter.PotStateFilter
import com.home.autowatering.service.impl.PotServiceImpl
import com.home.autowatering.service.impl.PotStateServiceImpl
import com.home.autowatering.service.impl.WateringSystemServiceImpl
import com.home.autowatering.service.interfaces.PotService
import com.home.autowatering.service.interfaces.PotStateService
import com.home.autowatering.service.interfaces.WateringSystemService
import java.util.*

//@RestController
//@RequestMapping("/pot")
class PotController(
    private val potService: PotService = PotServiceImpl(),
    private val potStateService: PotStateService = PotStateServiceImpl(),
    private val wateringSystemService: WateringSystemService = WateringSystemServiceImpl()
) : AbstractController() {

    //    @GetMapping("/list")
    fun list(): Response<List<PotDto>> =
        with(potService.findAll()
            .map { pot ->
                pot.apply {
                    this.humidity = potStateService.last(this)?.humidity
                }
            }
        ) {
            PotConverter.response(this)
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
        return PotStateConverter.response(
            potStateService.find(PotStateFilter(pot, dateFrom, dateTo))
        )
    }

    //    @PostMapping("/state/save")
    fun saveState(
//    @RequestBody
        request: PotStateDto
    ): Response<PotStateDto> {
        var state = PotStateConverter.fromDto(request)
        return PotStateConverter.response(
            potStateService.save(state)
        )
    }

}