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
import org.apache.commons.lang.Validate
import java.time.ZonedDateTime

class PotController(
    private val potService: PotService,
    private val potStateService: PotStateService,
    private val wateringSystemService: WateringSystemService
) : AbstractController() {

    fun list(): Response<List<PotDto>> =
        execute {
            val found = potService.findAll()
                .map { pot ->
                    pot.apply {
                        humidity = potStateService.last(this)?.humidity
                    }
                }
            PotConverter.response(found)
        }

    fun info(potCode: String): Response<PotDto> =
        execute {
            val pot = potService.find(PotFilter(code = potCode))
                .singleOrNull() ?: throw PotNotFoundException(potCode)
            val state = potStateService.last(pot)
            PotConverter.response(pot, state)
        }

    fun save(request: PotDto): Response<PotDto> =
        execute {
            Validate.noNullElements(
                arrayOf(
                    request.code, request.name, request.wateringDuration,
                    request.checkInterval, request.minHumidity
                )
            )
            val saved = potService.find(
                PotFilter(id = request.id, code = request.code)
            ).singleOrNull()
            val pot = if (saved == null) PotConverter.fromDto(request)
            else potService.merge(PotConverter.fromDto(request), saved)
            with(potService.save(pot)) {
                wateringSystemService.refresh(pot)
                PotConverter.response(pot)
            }
        }

    //    @GetMapping("/statistic/{pot}")
    fun statistic(
//        @PathVariable(value = "pot")
        potCode: String,
//        @RequestParam(
//            value = "dateFrom",
//            required = false
//        ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        dateFrom: ZonedDateTime?,
//        @RequestParam(
//            value = "dateTo",
//            required = false
//        ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        dateTo: ZonedDateTime?
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
    ): Response<PotStateDto> =
        execute {
            val state = PotStateConverter.fromDto(request)
            PotStateConverter.response(
                potStateService.save(state)
            )
        }
}