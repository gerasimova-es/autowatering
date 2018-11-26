package com.home.autowatering.dao.interfaces

import com.home.autowatering.entity.PotStateData

interface PotDao {
    fun save(potName: String, humidity: Double): PotStateData
}