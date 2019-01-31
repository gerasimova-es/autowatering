package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.business.TankState

interface TankStateDao {
    fun save(tankState: TankState): TankState
}