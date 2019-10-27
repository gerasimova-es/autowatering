package com.home.autowatering.dao.rest

import com.home.autowatering.config.BoardConfig
import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.exception.BoardNotAvailableException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.util.toJson
import io.vertx.core.Future


class WateringSystemRest(
    private val board: BoardConfig
) : WateringSystemDao {
    companion object {
        private const val REFRESH_SERVICE = "/pot/settings/save"
    }

    override fun refresh(pot: Pot): Future<Pot> =
        //todo execute blocking or WebClient async call
        Future.future<Pot> { future ->
            val response = khttp.post(
                url = board.url + REFRESH_SERVICE,
                json = PotConverter.fromEntity(pot).toJson()
            )
            when (response.statusCode) {
                200 -> future.complete(pot)
                else -> future.fail(BoardNotAvailableException(response.statusCode.toString()))
            }
        }
}