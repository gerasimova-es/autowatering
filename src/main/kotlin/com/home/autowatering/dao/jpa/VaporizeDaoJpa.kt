package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.SettingsNotFoundException
import com.home.autowatering.dao.VaporiseDao
import com.home.autowatering.dao.jpa.converter.JpaVaporizeConverter
import com.home.autowatering.dao.repository.VaporizeRepository
import com.home.autowatering.model.settings.Vaporizer
import org.springframework.stereotype.Repository

@Repository
class VaporizeDaoJpa(private var repository: VaporizeRepository) : VaporiseDao {

    override fun get(): Vaporizer {
        val settings = repository.findAll().stream()
            .findFirst()
            .orElseThrow { SettingsNotFoundException("vaporizer settings not found") }
        return JpaVaporizeConverter.fromJpa(settings)
    }

    override fun save(vaporizer: Vaporizer) {
        repository.save(JpaVaporizeConverter.fromEntity(vaporizer))
    }
}