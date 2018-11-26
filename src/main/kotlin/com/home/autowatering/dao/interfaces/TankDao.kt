package com.home.autowatering.dao.interfaces

import com.home.autowatering.entity.TankData

interface TankDao {
    fun save(volume: Double): TankData
}