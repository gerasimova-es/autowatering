package com.home.autowatering.api.rest

import com.home.autowatering.service.WateringService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/watering")
class WateringController(private var wateringService: WateringService) : AbstractController() {

    @GetMapping("/force")
    fun force(): String {
        LOGGER.info("request watering is received.")
        GlobalScope.launch {
            wateringService.watering()
            LOGGER.info("watering completed")

        }
        return OK
    }
}