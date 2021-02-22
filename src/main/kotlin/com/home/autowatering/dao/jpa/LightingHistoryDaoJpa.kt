package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.LightingHistoryDao
import com.home.autowatering.dao.jpa.converter.JpaLightingHistoryConverter
import com.home.autowatering.dao.repository.LightingHistoryRepository
import com.home.autowatering.model.history.LightingHistory
import org.springframework.stereotype.Repository

@Repository
class LightingHistoryDaoJpa(private var repository: LightingHistoryRepository) : LightingHistoryDao {

    override fun save(history: LightingHistory) {
        repository.save(JpaLightingHistoryConverter.fromEntity(history))
    }
}