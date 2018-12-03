package com.home.autowatering.controller

import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.dto.response.SendStateResponse
import com.home.autowatering.model.WateringState
import com.home.autowatering.service.impl.WateringStateServiceImpl
import com.home.autowatering.service.interfaces.WateringStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WateringStateController(
    private val stateService: WateringStateService
) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(WateringStateServiceImpl::class.java)
    }

    @GetMapping("/wateringstate/send")
    fun sendState(
        @RequestParam(value = "potName") potName: String,
        @RequestParam(value = "potHumidity") potHumidity: Double,
        @RequestParam(value = "tankName") tankName: String,
        @RequestParam(value = "tankVolume") tankVolume: Double
    ): SendStateResponse {

        try {
            stateService.load(
                WateringState(
                    potName,
                    potHumidity,
                    tankName,
                    tankVolume
                )
            )
            logger.info("watering state saving loaded successfully")

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