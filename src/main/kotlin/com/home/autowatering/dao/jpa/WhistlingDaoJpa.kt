package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.SettingsNotFoundException
import com.home.autowatering.dao.WhistlingDao
import com.home.autowatering.dao.jpa.converter.JpaWhistlingConverter
import com.home.autowatering.dao.repository.WhistlingRepository
import com.home.autowatering.model.settings.Whistling
import org.springframework.stereotype.Repository

@Repository
class WhistlingDaoJpa(private var repository: WhistlingRepository) : WhistlingDao {

    override fun get(): Whistling {
        val settings = repository.findAll().stream()
            .findFirst()
            .orElseThrow { SettingsNotFoundException("whistling settings not found") }
        return JpaWhistlingConverter.fromJpa(settings)
    }

    override fun save(whistling: Whistling) {
        repository.save(JpaWhistlingConverter.fromEntity(whistling))
    }


}