package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.SettingsNotFoundException
import com.home.autowatering.dao.WateringDao
import com.home.autowatering.dao.jpa.converter.JpaWateringConverter
import com.home.autowatering.dao.repository.WateringRepository
import com.home.autowatering.model.settings.Watering
import org.springframework.stereotype.Repository

@Repository
class WateringDaoJpa(private var repository: WateringRepository) : WateringDao {

    override fun get(): Watering {
        val settings = repository.findAll().singleOrNull()
            ?: throw SettingsNotFoundException("watering settings not found")
        return JpaWateringConverter.fromJpa(settings)
    }

    override fun save(watering: Watering) {
        repository.save(JpaWateringConverter.fromEntity(watering))
    }
}