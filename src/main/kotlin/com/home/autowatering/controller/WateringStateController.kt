package com.home.autowatering.controller

import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.dto.response.SendStateResponse
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.TankState
import com.home.autowatering.service.interfaces.PotStateService
import com.home.autowatering.service.interfaces.TankStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class WateringStateController(
    private val potStateService: PotStateService,
    private val tankStateService: TankStateService
) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(WateringStateController::class.java)
    }

    @GetMapping("/wateringstate/send")
    fun sendState(
        @RequestParam(value = "potName") potName: String,
        @RequestParam(value = "potHumidity") potHumidity: Double,
        @RequestParam(value = "tankName") tankName: String,
        @RequestParam(value = "tankVolume") tankVolume: Double,
        @RequestParam(value = "tankFilled") tankFilled: Double
    ): SendStateResponse {
        logger.info("received send watering state request. Executing...")
        val requestDate = Date()
        try {
            potStateService.save(PotState(Pot(potName), requestDate, potHumidity))
            logger.info("pot state loaded successfully. Loading tank state...")

            tankStateService.save(TankState(tankName, requestDate, tankVolume, tankFilled))
            logger.info("tank state loaded successfully")

            return response()

        } catch (exc: Exception) {
            logger.error("watering state saving error: ", exc)
            return response(exc)
        }
    }

    private fun response(): SendStateResponse =
        SendStateResponse("OK")

    private fun response(error: Exception) =
        SendStateResponse(ResponseStatus.ERROR, if (error.message == null) error.javaClass.name else error.message!!)
}