package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.LightingDao
import com.home.autowatering.dao.SettingsNotFoundException
import com.home.autowatering.dao.jpa.converter.JpaLightingConverter
import com.home.autowatering.dao.repository.LightingRepository
import com.home.autowatering.model.settings.Lighting
import org.springframework.stereotype.Repository

@Repository
class LightingDaoJpa(private var repository: LightingRepository) : LightingDao {

    override fun get(): Lighting {
        val settings = repository.findAll().singleOrNull()
            ?: throw SettingsNotFoundException("lighting settings not found")
        return JpaLightingConverter.fromJpa(settings)
    }

    override fun save(lighting: Lighting) {
        repository.save(JpaLightingConverter.fromEntity(lighting))
    }

}