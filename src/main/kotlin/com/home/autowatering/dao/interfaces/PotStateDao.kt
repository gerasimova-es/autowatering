package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter

interface PotStateDao {
    fun find(filter: PotStateFilter): List<PotState>
    fun save(state: PotState): PotState
}