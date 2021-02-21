package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.LightingSettingsDao
import com.home.autowatering.dao.SettingsNotFoundException
import com.home.autowatering.dao.jpa.converter.JpaLightingSettingsConverter
import com.home.autowatering.dao.repository.LightingSettingsRepository
import com.home.autowatering.model.LightingSettings
import org.springframework.stereotype.Repository

@Repository
class LightingSettingsDaoJpa(private var repository: LightingSettingsRepository) : LightingSettingsDao {

    override fun get(): LightingSettings {
        val settings = repository.findAll().stream()
            .findFirst()
            .orElseThrow { SettingsNotFoundException("lighting settings not found") }
        return JpaLightingSettingsConverter.fromJpa(settings)
    }

}