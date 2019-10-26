package com.home.autowatering.controller

import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.controller.converter.PotStateConverter
import com.home.autowatering.controller.dto.PotDto
import com.home.autowatering.controller.dto.PotStateDto
import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.exception.IncorrectPeriodException
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.business.filter.PotFilter
import com.home.autowatering.model.business.filter.PotStateFilter
import com.home.autowatering.model.business.filter.SliceType
import com.home.autowatering.service.interfaces.PotService
import com.home.autowatering.service.interfaces.PotStateService
import com.home.autowatering.service.interfaces.WateringSystemService
import com.home.autowatering.validator.PeriodValidator
import com.home.autowatering.validator.onError
import io.vertx.core.Future
import java.time.ZonedDateTime

class PotController(
    private val potService: PotService,
    private val potStateService: PotStateService,
    private val wateringSystemService: WateringSystemService
) : AbstractController() {

    fun list(): Future<Response<List<PotDto>>> =
        //todo use execute, check failing{}
        potService.findAll()
            .compose<Response<List<PotDto>>> { pots ->
                Future.future<Response<List<PotDto>>>().apply {
                    complete(PotConverter.response(pots))
                }
            }

    fun info(potCode: String): Future<Response<PotDto>> =
        //todo use execute, check failing{}
        potService.find(PotFilter(code = potCode))
            .compose<Response<PotDto>> { pots ->
                Future.future<Response<PotDto>>().apply {
                    val pot = pots.singleOrNull() ?: throw PotNotFoundException(potCode)
                    val state = potStateService.last(pot)
                    complete(PotConverter.response(pot, state))
                }
            }

    fun save(request: PotDto): Future<Response<PotDto>> =
        //todo use pot validator, execute{}, check failing{}
        potService.find(
            PotFilter(id = request.id, code = request.code)
        ).compose<Pot> { pots ->
            Future.future<Pot>().apply {
                val saved = pots.singleOrNull()
                val pot = if (saved == null) PotConverter.fromDto(request)
                else potService.merge(PotConverter.fromDto(request), saved)
                complete(pot)
            }
        }.compose<Pot> { pot ->
            Future.future<Pot>().apply {
                complete(potService.save(pot))
            }
        }.compose<Pot> { pot ->
            Future.future<Pot>().apply {
                wateringSystemService.refresh(pot)
                complete(pot)
            }
        }.compose<Response<PotDto>> { pot ->
            Future.future<Response<PotDto>>().apply {
                complete(PotConverter.response(pot))
            }
        }


    fun saveState(
        request: PotStateDto
    ): Future<Response<PotStateDto>> =
        //todo use execute, check failing{}
        Future.future<PotState>().apply {
            val state = PotStateConverter.fromDto(request)
            complete(potStateService.save(state))
        }.compose<Response<PotStateDto>> {
            Future.future<Response<PotStateDto>>().apply {
                complete(PotStateConverter.response())
            }
        }


    fun statistic(
        potCode: String,
        dateFrom: ZonedDateTime?,
        dateTo: ZonedDateTime?,
        slice: SliceType = SliceType.MINUTE
    ): Future<Response<List<PotStateDto>>> =
        //todo use execute, check failing{}
        Future.future<Void>() {
            PeriodValidator(dateFrom, dateTo, slice).validate()
                .onError { result -> throw IncorrectPeriodException(result.message) }
            it.complete()
        }.compose<List<Pot>> {
            potService.find(PotFilter(code = potCode))
        }.compose<Response<List<PotStateDto>>> { pots ->
            val pot = pots.singleOrNull() ?: throw PotNotFoundException(potCode)
            Future.future<Response<List<PotStateDto>>>().apply {
                complete(
                    PotStateConverter.response(
                        potStateService.find(
                            PotStateFilter(pot, dateFrom, dateTo, slice)
                        )
                    )
                )
            }
        }


}