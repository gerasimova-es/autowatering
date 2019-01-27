package com.home.autowatering.controller

import com.home.autowatering.converter.TankStateConverter
import com.home.autowatering.dto.TankStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.service.interfaces.TankStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tank")
class TankStateController(val tankStateService: TankStateService) : AbstractController() {
    val converter = TankStateConverter()

    companion object {
        val logger: Logger = LoggerFactory.getLogger(TankStateController::class.java)
    }

    @PostMapping("/state/save")
    fun save(@RequestBody request: TankStateDto): Response<TankStateDto> {
        logger.info("received saving state request [$request]. Executing...")
        val result = tankStateService.save(converter.fromDto(request))
        logger.info("state [$result] saved successfully")
        return converter.response(result)
    }
}