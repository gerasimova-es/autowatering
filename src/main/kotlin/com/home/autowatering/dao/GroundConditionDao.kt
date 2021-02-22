package com.home.autowatering.dao

import com.home.autowatering.model.history.GroundCondition

interface GroundConditionDao {

    fun save(condition: GroundCondition)
}