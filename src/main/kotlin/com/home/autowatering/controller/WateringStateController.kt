package com.home.autowatering.controller

import com.home.autowatering.dto.SendStateResult
import com.home.autowatering.model.WateringState
import com.home.autowatering.service.interfaces.WateringStateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WateringStateController(private val stateService: WateringStateService) {
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
    ): SendStateResult =
        try {
            stateService.load(WateringState(potName, humidity, tankName, tankVolume))
            response()
        } catch (exc: Exception) {
            response(exc)
        }
}