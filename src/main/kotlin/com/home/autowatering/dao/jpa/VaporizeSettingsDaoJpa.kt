package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.SettingsNotFoundException
import com.home.autowatering.dao.VaporiseSettingsDao
import com.home.autowatering.dao.jpa.converter.JpaVaporizeSettingsConverter
import com.home.autowatering.dao.repository.VaporizeSettingsRepository
import com.home.autowatering.model.VaporizeSettings
import org.springframework.stereotype.Repository

@Repository
class VaporizeSettingsDaoJpa(private var repository: VaporizeSettingsRepository) : VaporiseSettingsDao {

    override fun get(): VaporizeSettings {
        val settings = repository.findAll().stream()
            .findFirst()
            .orElseThrow { SettingsNotFoundException("vaporize settings not found") }
        return JpaVaporizeSettingsConverter.fromJpa(settings)
    }
}