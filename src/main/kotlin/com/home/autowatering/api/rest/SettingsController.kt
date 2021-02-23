package com.home.autowatering.api.rest;

import com.home.autowatering.api.converter.DeviceSettingsDtoConverter
import com.home.autowatering.api.dto.settings.DeviceSettingsDto
import com.home.autowatering.model.settings.DeviceSettings
import com.home.autowatering.service.DeviceService
import com.home.autowatering.service.LightingService
import com.home.autowatering.service.VaporizeService
import com.home.autowatering.service.WateringService
import com.home.autowatering.service.WhistleService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/settings")
class SettingsController(
    var lightingService: LightingService,
    var vaporizeService: VaporizeService,
    var whistleService: WhistleService,
    var wateringService: WateringService,
    val deviceService: DeviceService
) : AbstractController() {

    @GetMapping("/info")
    fun info(): DeviceSettingsDto {
        val settings = DeviceSettings(
            lightingService.getSettings(),
            wateringService.getSettings(),
            whistleService.getSettings(),
            vaporizeService.getSettings()
        )
        return DeviceSettingsDtoConverter.fromEntity(settings);
    }

    @PostMapping("/update")
    fun update(@RequestBody settingsDto: DeviceSettingsDto): String {
        LOGGER.info("request for update settings is received.")
        val settings = DeviceSettingsDtoConverter.fromDto(settingsDto)

        settings.watering?.let {
            wateringService.saveSettings(it)
        }
        settings.whistling?.let {
            whistleService.saveSettings(it)
        }
        settings.lighting?.let {
            lightingService.saveSettings(it)
        }
        settings.vaporizer?.let {
            vaporizeService.saveSettings(it)
        }

        LOGGER.info("sending request for update settings to device")
        GlobalScope.launch {
            runCatching { deviceService.refresh() }
                .onFailure { error -> LOGGER.error("error during update settings on device", error) }
                .onSuccess { LOGGER.info("settings updated on device") }
        }

        return OK
    }

}
