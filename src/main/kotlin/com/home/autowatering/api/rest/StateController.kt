package com.home.autowatering.api.rest

import com.home.autowatering.invoker.DeviceInvoker
import com.home.autowatering.invoker.converter.DeviceStateConverter
import com.home.autowatering.invoker.dto.state.DeviceStateDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/state")
class StateController(private var wateringSystemInvoker: DeviceInvoker) : AbstractController() {

    @GetMapping("/info")
    fun info(): DeviceStateDto {
        return DeviceStateConverter.fromEntity(wateringSystemInvoker.getState())
    }
}