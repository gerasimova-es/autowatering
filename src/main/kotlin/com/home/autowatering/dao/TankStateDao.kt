package com.home.autowatering.dao

import com.home.autowatering.model.business.TankState

interface TankStateDao {
    fun save(tankState: TankState): TankState
}