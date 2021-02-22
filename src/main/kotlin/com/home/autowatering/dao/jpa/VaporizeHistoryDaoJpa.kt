package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.VaporizeHistoryDao
import com.home.autowatering.dao.jpa.converter.JpaVaporizeHistoryConverter
import com.home.autowatering.dao.repository.VaporizeHistoryRepository
import com.home.autowatering.model.history.VaporizerHistory
import org.springframework.stereotype.Repository

@Repository
class VaporizeHistoryDaoJpa(private var repository: VaporizeHistoryRepository) : VaporizeHistoryDao {

    override fun save(history: VaporizerHistory) {
        repository.save(JpaVaporizeHistoryConverter.fromEntity(history))
    }
}