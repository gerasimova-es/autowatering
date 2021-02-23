package com.home.autowatering.api.converter

import com.home.autowatering.api.dto.settings.DeviceSettingsDto
import com.home.autowatering.api.dto.settings.LightingDto
import com.home.autowatering.api.dto.settings.VaporizeDto
import com.home.autowatering.api.dto.settings.WateringDto
import com.home.autowatering.api.dto.settings.WhistlingDto
import com.home.autowatering.model.settings.DeviceSettings
import com.home.autowatering.model.settings.Lighting
import com.home.autowatering.model.settings.Vaporizer
import com.home.autowatering.model.settings.Watering
import com.home.autowatering.model.settings.Whistling
import java.time.LocalTime

object DeviceSettingsDtoConverter : RequestConverter<DeviceSettingsDto, DeviceSettings>(
    {
        DeviceSettings(
            lighting = it.lighting?.let { lighting ->
                Lighting(
                    lighting.enabled,
                    LocalTime.of(lighting.startHour, lighting.startMinute),
                    LocalTime.of(lighting.stopHour, lighting.stopMinute)
                )
            },
            watering = it.watering?.let { watering ->
                Watering(
                    enabled = watering.enabled,
                    minHumidity = watering.minHumidity,
                    checkInterval = watering.interval,
                    duration = watering.duration
                )
            },
            whistling = it.whistling?.let { whistling ->
                Whistling(
                    whistling.enabled,
                    whistling.duration,
                    LocalTime.of(whistling.startHour, whistling.startMinute),
                    LocalTime.of(whistling.stopHour, whistling.stopMinute)
                )
            },
            vaporizer = it.vaporize?.let { vaporize ->
                Vaporizer(
                    vaporize.enabled,
                    vaporize.minHumidity,
                    vaporize.interval
                )
            }

        )
    },
    {
        DeviceSettingsDto(
            watering = it.watering?.let { watering ->
                WateringDto(
                    watering.enabled,
                    watering.minHumidity,
                    watering.checkInterval,
                    watering.duration
                )
            },
            vaporize = it.vaporizer?.let { vaporise ->
                VaporizeDto(
                    vaporise.enabled,
                    vaporise.minHumidity,
                    vaporise.checkInterval
                )
            },
            lighting = it.lighting?.let { lighting ->
                LightingDto(
                    lighting.enabled,
                    lighting.startTime.hour,
                    lighting.startTime.minute,
                    lighting.stopTime.hour,
                    lighting.stopTime.minute
                )
            },
            whistling = it.whistling?.let { whistling ->
                WhistlingDto(
                    whistling.enabled,
                    whistling.duration,
                    whistling.startTime.hour,
                    whistling.startTime.minute,
                    whistling.stopTime.hour,
                    whistling.stopTime.minute
                )
            }
        )
    }
)