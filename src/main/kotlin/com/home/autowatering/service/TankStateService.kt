package com.home.autowatering.service

import com.home.autowatering.model.business.TankState

interface TankStateService {
    fun save(state: TankState): TankState
}