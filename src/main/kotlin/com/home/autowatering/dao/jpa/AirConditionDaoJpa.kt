package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.AirConditionDao
import com.home.autowatering.dao.jpa.converter.JpaAirConditionConverter
import com.home.autowatering.dao.repository.AirConditionRepository
import com.home.autowatering.model.history.AirCondition
import org.springframework.stereotype.Repository

@Repository
class AirConditionDaoJpa(private var repository: AirConditionRepository) : AirConditionDao {

    override fun save(condition: AirCondition) {
        repository.save(JpaAirConditionConverter.fromEntity(condition))
    }
}