package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.SettingsNotFoundException
import com.home.autowatering.dao.WateringSettingsDao
import com.home.autowatering.dao.jpa.converter.JpaWateringSettingsConverter
import com.home.autowatering.dao.repository.WateringSettingsRepository
import com.home.autowatering.model.WateringSettings
import org.springframework.stereotype.Repository

@Repository
class WateringSettingsDaoJpa(private var repository: WateringSettingsRepository) : WateringSettingsDao {

    override fun get(): WateringSettings {
        val settings = repository.findAll().stream()
            .findFirst()
            .orElseThrow { SettingsNotFoundException("watering settings not found") }
        return JpaWateringSettingsConverter.fromJpa(settings)
    }
}