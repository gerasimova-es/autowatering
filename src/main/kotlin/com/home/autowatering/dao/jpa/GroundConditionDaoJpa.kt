package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.GroundConditionDao
import com.home.autowatering.dao.jpa.converter.JpaGroundConditionConverter
import com.home.autowatering.dao.repository.GroundConditionRepository
import com.home.autowatering.model.history.GroundCondition
import org.springframework.stereotype.Repository

@Repository
class GroundConditionDaoJpa(private var repository: GroundConditionRepository) : GroundConditionDao{

    override fun save(condition: GroundCondition) {
        repository.save(JpaGroundConditionConverter.fromEntity(condition))
    }

}