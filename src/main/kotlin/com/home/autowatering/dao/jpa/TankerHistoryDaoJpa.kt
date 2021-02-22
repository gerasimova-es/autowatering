package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.TankerHistoryDao
import com.home.autowatering.dao.jpa.converter.JpaTankerHistoryConverter
import com.home.autowatering.dao.repository.TankerHistoryRepository
import com.home.autowatering.model.history.TankerHistory
import org.springframework.stereotype.Repository

@Repository
class TankerHistoryDaoJpa(private var repository: TankerHistoryRepository) : TankerHistoryDao {

    override fun save(history: TankerHistory) {
        repository.save(JpaTankerHistoryConverter.fromEntity(history))
    }


}