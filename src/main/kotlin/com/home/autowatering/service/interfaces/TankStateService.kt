package com.home.autowatering.service.interfaces

import com.home.autowatering.model.TankState

interface TankStateService {
    fun save(state: TankState): TankState
}