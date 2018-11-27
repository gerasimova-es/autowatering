package com.home.autowatering.dao.interfaces

import com.home.autowatering.entity.PotStateData

interface PotStateDao {
    fun save(potName: String, humidity: Double): PotStateData
}