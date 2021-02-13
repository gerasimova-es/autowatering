package com.home.autowatering.dao

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.business.filter.PotStateFilter

interface PotStateDao {
    fun find(filter: PotStateFilter): List<PotState>
    fun last(pot: Pot): PotState?
    fun save(state: PotState): PotState
}