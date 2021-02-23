package com.home.autowatering.service

import com.home.autowatering.dao.AirConditionDao
import com.home.autowatering.dao.GroundConditionDao
import com.home.autowatering.model.history.AirCondition
import com.home.autowatering.model.history.GroundCondition
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ClimateService(
    private var airConditionDao: AirConditionDao,
    private var groundConditionDao: GroundConditionDao
) {

    @Transactional
    fun saveAirCondition(condition: AirCondition) {
        airConditionDao.save(condition)
    }

    @Transactional
    fun saveGroundCondition(condition: GroundCondition) {
        groundConditionDao.save(condition)
    }
}