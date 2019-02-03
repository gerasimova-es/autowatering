package com.home.autowatering.service.interfaces

import com.home.autowatering.model.business.Pot

interface WateringSystemService {
    fun refresh(pot: Pot)
}