package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.business.Pot

interface WateringSystemDao {
    fun refresh(pot: Pot)
}