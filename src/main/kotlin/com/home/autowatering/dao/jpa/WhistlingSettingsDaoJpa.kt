package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.SettingsNotFoundException
import com.home.autowatering.dao.WhistlingSettingsDao
import com.home.autowatering.dao.jpa.converter.JpaWhistlingSettingsConverter
import com.home.autowatering.dao.repository.WhistlingSettingsRepository
import com.home.autowatering.model.WhistlingSettings
import org.springframework.stereotype.Repository

@Repository
class WhistlingSettingsDaoJpa(private var repository: WhistlingSettingsRepository) : WhistlingSettingsDao {

    override fun get(): WhistlingSettings {
        val settings = repository.findAll().stream()
            .findFirst()
            .orElseThrow { SettingsNotFoundException("whistling settings not found") }
        return JpaWhistlingSettingsConverter.fromJpa(settings)
    }
}