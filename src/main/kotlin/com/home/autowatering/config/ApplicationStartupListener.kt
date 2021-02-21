package com.home.autowatering.config

import com.home.autowatering.dao.jpa.entity.JpaLightingSettings
import com.home.autowatering.dao.repository.LightingSettingsRepository
import org.springframework.stereotype.Component
import java.time.LocalTime
import javax.annotation.PostConstruct

//@Component
class ApplicationStartupListener(private var lightingSettingsRepository: LightingSettingsRepository) {

//    @PostConstruct
    fun init() {
        println("loading lighting settings...")
        lightingSettingsRepository.save(
            JpaLightingSettings(
                100,
                true,
                LocalTime.of(8, 0),
                LocalTime.of(23, 0)
            )
        )
    }
}