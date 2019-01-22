package com.home.autowatering.controller

import com.home.autowatering.converter.TankStateConverter
import com.home.autowatering.dto.TankStateDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.service.interfaces.TankStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TankStateController(val tankStateService: TankStateService) : AbstractController() {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(PotStateController::class.java)
    }

    @GetMapping("/tankstate/save")
    fun save(@RequestBody request: TankStateDto): Response<TankStateDto> {
        logger.info("received saving state request. Executing...")
        val converter = TankStateConverter()
        val result = tankStateService.save(converter.fromDto(request))
        logger.info("state [$result] saved successfully")
        return converter.response(result)
    }

}