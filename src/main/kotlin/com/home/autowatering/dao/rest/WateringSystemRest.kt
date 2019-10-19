package com.home.autowatering.dao.rest

import com.home.autowatering.Application
import com.home.autowatering.config.BoardConfig
import com.home.autowatering.controller.converter.PotConverter
import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.model.business.Pot
import io.vertx.ext.web.client.WebClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class WateringSystemRest(private val client: WebClient, private val board: BoardConfig) : WateringSystemDao {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(Application::class.java)
        private const val REFRESH_SERVICE = "/pot/settings/save"
    }

    override fun refresh(pot: Pot) =
        client.post(board.url, REFRESH_SERVICE)
            .sendJson(PotConverter.fromEntity(pot)) {
                if (it.succeeded()) {
                    val response = it.result()
                    LOGGER.debug("Received response with status code {}", response?.statusCode());
                } else {
                    LOGGER.error("Something went wrong", it.cause())
                }
            }
}