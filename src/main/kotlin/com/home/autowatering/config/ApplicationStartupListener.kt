package com.home.autowatering.config

import com.home.autowatering.dao.jpa.entity.JpaLighting
import com.home.autowatering.dao.repository.LightingRepository
import java.time.LocalTime

//@Component
class ApplicationStartupListener(private var lightingRepository: LightingRepository) {

//    @PostConstruct
    fun init() {
        println("loading lighting settings...")
        lightingRepository.save(
            JpaLighting(
                100,
                true,
                LocalTime.of(8, 0),
                LocalTime.of(23, 0)
            )
        )
    }
}