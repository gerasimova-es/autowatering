package com.home.autowatering.controller

import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.dto.response.SearchPotStateResponse
import com.home.autowatering.model.TankState
import com.home.autowatering.service.interfaces.TankStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class TankStateController(val tankStateService: TankStateService) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(PotStateController::class.java)
    }

    @GetMapping("/tankstate/save")
    fun save(
        @RequestParam(value = "name") name: String,
        @RequestParam(value = "volume") volume: Double,
        @RequestParam(value = "filled") filled: Double
    ): Any { //todo special DTO
        logger.info("received saving state request. Executing...")
        val requestDate = Date()
        try {
            val result = tankStateService.save(
                TankState(
                    name = name, date = requestDate, volume = volume, filled = filled
                )
            )
            logger.info("state [$result] saved successfully")
            return response(result)
        } catch (exc: Exception) {
            logger.error("request executing error")
            return response(exc)
        }
    }

    //todo to AbstractController
    private fun response(exc: Exception) =
        SearchPotStateResponse(ResponseStatus.ERROR, if (exc.message == null) exc.javaClass.name else exc.message!!)

    //todo specify
    private fun response(result: Any): Any = Any()

}