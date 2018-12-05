package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter

interface PotStateDao {
    fun save(state: PotState): PotState
    fun find(filter: PotStateFilter): List<PotState>
}