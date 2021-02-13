package com.home.autowatering.dao

import com.home.autowatering.model.business.Pot

interface WateringSystemDao {
    fun refresh(pot: Pot)
}