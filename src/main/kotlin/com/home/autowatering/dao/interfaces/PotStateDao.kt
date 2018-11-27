package com.home.autowatering.dao.interfaces

import com.home.autowatering.entity.PotStateData
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter

interface PotStateDao {
    fun save(pot: Pot, humidity: Double): PotStateData
    fun find(filter: PotStateFilter): List<PotState>
}