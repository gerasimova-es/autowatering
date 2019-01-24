package com.home.autowatering.controller

import com.home.autowatering.converter.PotConverter
import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.response.Response
import com.home.autowatering.service.interfaces.PotService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pot")
class PotController(var potService: PotService) : AbstractController() {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(PotController::class.java)
    }

    @PostMapping("/save")
    fun save(@RequestBody request: PotDto): Response<PotDto> {
        LOGGER.info("received saving pot request. Executing...")

        val converter = PotConverter()
        val pot = potService.save(converter.fromDto(request))

        LOGGER.info("pot [$pot] saved successfully")
        return converter.response(pot)
    }


    @GetMapping("/{id}")
    fun get(@PathVariable(value = "id") id: Long): Response<PotDto> {
        LOGGER.info("received search pot by id request. Search executing...")
        val pot = potService.getById(id)
        LOGGER.info("found pot [${pot}] ")
        return PotConverter().response(pot)
    }
}