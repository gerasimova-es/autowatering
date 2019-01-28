package com.home.autowatering.controller

import com.home.autowatering.converter.TankStateConverter
import com.home.autowatering.dto.TankStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.service.interfaces.TankStateService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tank")
class TankStateController(val tankStateService: TankStateService) : AbstractController() {
    val converter = TankStateConverter()

    @PostMapping("/state/save")
    fun save(@RequestBody request: TankStateDto): Response<TankStateDto> {
        val result = tankStateService.save(converter.fromDto(request))
        return converter.response(result)
    }
}