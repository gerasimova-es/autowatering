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
import io.vertx.core.Future.future
import java.time.ZonedDateTime

class PotController(
    private val potService: PotService,
    private val potStateService: PotStateService,
    private val wateringSystemService: WateringSystemService
) : AbstractController() {

    fun list(): Future<Response<List<PotDto>>> =
        potService.findAll()
            .compose<Response<List<PotDto>>> { pots ->
                future<Response<List<PotDto>>> { future ->
                    future.complete(PotConverter.response(pots))
                }
            }.handleFailure()

    fun info(potCode: String): Future<Response<PotDto>> =
        potService.find(PotFilter(code = potCode))
            .compose<Response<PotDto>> { pots ->
                future<Response<PotDto>> { future ->
                    val pot = pots.singleOrNull() ?: throw PotNotFoundException(potCode)
                    val state = potStateService.last(pot)
                    future.complete(PotConverter.response(pot, state))
                }
            }.handleFailure()

    fun save(request: PotDto): Future<Response<PotDto>> =
        //todo use pot validator
        potService.find(
            PotFilter(id = request.id, code = request.code)
        ).compose<Pot> { pots ->
            future<Pot> { future ->
                val saved = pots.singleOrNull()
                val pot = if (saved == null) PotConverter.fromDto(request)
                else potService.merge(PotConverter.fromDto(request), saved)
                future.complete(pot)
            }
        }.compose<Pot> { pot ->
            potService.save(pot)
        }.compose<Pot> { pot ->
            wateringSystemService.refresh(pot)
        }.compose<Response<PotDto>> { pot ->
            future<Response<PotDto>> { future ->
                future.complete(PotConverter.response(pot))
            }
        }//.handleFailure()

    fun saveState(
        request: PotStateDto
    ): Future<Response<PotStateDto>> =
        //todo use execute
        future<PotState> { future ->
            val state = PotStateConverter.fromDto(request)
            future.complete(potStateService.save(state))
        }.compose<Response<PotStateDto>> {
            future<Response<PotStateDto>> { future ->
                future.complete(PotStateConverter.response())
            }
        }//.handleFailure()

    fun statistic(
        potCode: String,
        dateFrom: ZonedDateTime?,
        dateTo: ZonedDateTime?,
        slice: SliceType = SliceType.MINUTE
    ): Future<Response<List<PotStateDto>>> =
        //todo use execute, check failing{}
        future<Void> {
            PeriodValidator(dateFrom, dateTo, slice).validate()
                .onError { result -> throw IncorrectPeriodException(result.message) }
            it.complete()
        }.compose<List<Pot>> {
            potService.find(PotFilter(code = potCode))
        }.compose<Response<List<PotStateDto>>> { pots ->
            val pot = pots.singleOrNull() ?: throw PotNotFoundException(potCode)
            future<Response<List<PotStateDto>>> { future ->
                future.complete(
                    PotStateConverter.response(
                        potStateService.find(
                            PotStateFilter(pot, dateFrom, dateTo, slice)
                        )
                    )
                )
            }
        }.handleFailure()
}