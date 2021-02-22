package com.home.autowatering.service

import com.home.autowatering.dao.AirConditionDao
import com.home.autowatering.dao.GroundConditionDao
import com.home.autowatering.model.history.AirCondition
import com.home.autowatering.model.history.GroundCondition
import org.springframework.stereotype.Service

@Service
class ClimateService(
    private var airConditionDao: AirConditionDao,
    private var groundConditionDao: GroundConditionDao
) {

    fun saveAirCondition(condition: AirCondition) {
        airConditionDao.save(condition)
    }

    fun saveGroundCondition(condition: GroundCondition) {
        groundConditionDao.save(condition)
    }
}