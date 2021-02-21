package com.home.autowatering.api.rest

import com.home.autowatering.invoker.WateringSystemInvoker
import com.home.autowatering.invoker.state.DeviceStateDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/state")
class DeviceStateController(private var wateringSystemInvoker: WateringSystemInvoker) : AbstractController() {

    @GetMapping("/info")
    fun info(): DeviceStateDto {
        return wateringSystemInvoker.getState()
    }
}