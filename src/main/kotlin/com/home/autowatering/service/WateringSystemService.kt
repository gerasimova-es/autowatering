package com.home.autowatering.service

import com.home.autowatering.model.business.Pot

interface WateringSystemService {
    fun refresh(pot: Pot)
}