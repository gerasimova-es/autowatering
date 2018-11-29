package com.home.autowatering.controller

import com.home.autowatering.dto.SendStateResult
import com.home.autowatering.model.Pot
import com.home.autowatering.model.WateringState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import com.home.autowatering.service.interfaces.WateringStateService
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
        fun response(): SendStateResult = SendStateResult()
        fun response(error: Exception) = SendStateResult("ERROR", error.message)
    }

    @GetMapping("/send-state")
    fun sendState(
        @RequestParam(value = "potName", defaultValue = "1") potName: String,
        @RequestParam(value = "humidity") humidity: Double,
        @RequestParam(value = "tankName", defaultValue = "1") tankName: String,
        @RequestParam(value = "tankVolume") tankVolume: Double
    ): Any {

        try {
            val from = Date()
            stateService.load(WateringState(potName, humidity, tankName, tankVolume))
            val to = Date()

            return potStateService.find(
                PotStateFilter.withPot(Pot(potName))
                    .from(from)
                    .to(to)
                    .build()
            )

        } catch (exc: Exception) {
            return response(exc)
        }
    }
}