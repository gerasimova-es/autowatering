package com.home.autowatering.controller

import com.home.autowatering.dto.SendStateResult
import com.home.autowatering.model.WateringState
import com.home.autowatering.service.impl.WateringStateServiceImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WateringStateController(val stateService: WateringStateServiceImpl) {
    companion object {
        fun response(): SendStateResult = SendStateResult()
        fun response(error: Exception) = SendStateResult("ERROR", error.message)
    }

    @GetMapping("/send-state")
    fun sendState(
        @RequestParam(value = "potName", defaultValue = "1") potName: String,
        @RequestParam(value = "humidity") humidity: Double,
        @RequestParam(value = "tankVolume") tankVolume: Double
    ): SendStateResult {
        try {
            stateService.load(WateringState(potName, humidity, tankVolume))
        } catch (exc: Exception) {
            return response(exc)
        }
        return response()
    }
}