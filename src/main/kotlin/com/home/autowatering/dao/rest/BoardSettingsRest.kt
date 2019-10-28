package com.home.autowatering.dao.rest

import com.home.autowatering.config.BoardConfig
import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.dao.interfaces.BoardSettingsDao
import com.home.autowatering.exception.BoardNotAvailableException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.util.toJson
import io.vertx.core.Future
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class BoardSettingsRest(
    private val board: BoardConfig
) : BoardSettingsDao {
    companion object {
        private const val REFRESH_SERVICE = "/pot/settings/save"
        private val LOGGER: Logger = LoggerFactory.getLogger(BoardSettingsRest::class.java)
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
                    .also { LOGGER.info("request is sent to board successfully") }
                else -> future.fail(BoardNotAvailableException(response.statusCode.toString()))
                    .also { LOGGER.error("sending request to board error: {}", response.statusCode) }
            }
        }
}