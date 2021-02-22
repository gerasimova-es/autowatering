package com.home.autowatering.api.rest;

import com.home.autowatering.api.converter.LightingSettingsConverter
import com.home.autowatering.api.converter.VaporizeSettingsConverter
import com.home.autowatering.api.converter.WateringSettingsConverter
import com.home.autowatering.api.converter.WhistlingSettingsConverter
import com.home.autowatering.api.dto.settings.DeviceSettingsDto
import com.home.autowatering.service.LightingService
import com.home.autowatering.service.VaporizeService
import com.home.autowatering.service.WateringService
import com.home.autowatering.service.WhistleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/settings")
class SettingsController(
    var lightingService: LightingService,
    var vaporizeService: VaporizeService,
    var whistleService: WhistleService,
    var wateringService: WateringService
) : AbstractController() {

    @GetMapping("/info")
    fun info(): DeviceSettingsDto {
        return DeviceSettingsDto(
            WateringSettingsConverter.fromEntity(wateringService.getSettings()),
            VaporizeSettingsConverter.fromEntity(vaporizeService.getSettings()),
            LightingSettingsConverter.fromEntity(lightingService.getSettings()),
            WhistlingSettingsConverter.fromEntity(whistleService.getSettings())
        )
    }

}
