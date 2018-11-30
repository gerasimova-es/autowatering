package com.home.autowatering.controller

import com.home.autowatering.dto.SendStateResult
import com.home.autowatering.model.Pot
import com.home.autowatering.model.WateringState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.impl.WateringStateServiceImpl
import com.home.autowatering.service.interfaces.PotStateService
import com.home.autowatering.service.interfaces.WateringStateService
import org.apache.commons.lang.Validate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class WateringStateController(
    private val stateService: WateringStateService,
    private val potStateService: PotStateService
) {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(WateringStateServiceImpl::class.java)
    }

    @GetMapping("/send-state")
    fun sendState(
        @RequestParam(value = "potName") potName: String,
        @RequestParam(value = "potHumidity") potHumidity: Double,
        @RequestParam(value = "tankName") tankName: String,
        @RequestParam(value = "tankVolume") tankVolume: Double
    ): Any {

        try {
            val wateringState =
                WateringState(potName, potHumidity, tankName, tankVolume) //todo think about json input parameter type

            validate(wateringState)

            val from = Date()
            stateService.load(wateringState)
            logger.info("watering state saving loaded successfully")
            val to = Date()

            //todo delete find and from/to
            return potStateService.find(
                PotStateFilter.withPot(Pot(potName))
                    .from(from)
                    .to(to)
                    .build()
            )

        } catch (exc: Exception) {
            logger.error("watering state saving error: ", exc)
            return response(exc)
        }
    }

    private fun response(): SendStateResult = SendStateResult() //todo use
    private fun response(error: Exception) = SendStateResult("ERROR", error.message)

    private fun validate(state: WateringState) {
        Validate.notNull(state)
        Validate.notEmpty(state.potName, "pot name cannot be empty")
        if (state.potHumidity <= 0) {
            throw IllegalArgumentException("pot humidity cannot be less or equals zero")
        }
        Validate.notEmpty(state.tankName, "tank name cannot be empty")
        if (state.tankVolume < 0) {
            throw IllegalArgumentException("tank volume cannot be less zero")
        }
    }

}