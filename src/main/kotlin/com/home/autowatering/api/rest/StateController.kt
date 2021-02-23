package com.home.autowatering.api.rest

import com.home.autowatering.invoker.converter.DeviceStateDtoConverter
import com.home.autowatering.invoker.dto.state.DeviceStateDto
import com.home.autowatering.service.DeviceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/state")
class StateController(private var deviceService: DeviceService) : AbstractController() {

    @GetMapping("/info")
    fun info(): DeviceStateDto {
        return DeviceStateDtoConverter.fromEntity(deviceService.getState())
    }
}