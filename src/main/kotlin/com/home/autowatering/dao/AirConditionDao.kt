package com.home.autowatering.dao

import com.home.autowatering.model.history.AirCondition

interface AirConditionDao {

    fun save(condition: AirCondition)
}