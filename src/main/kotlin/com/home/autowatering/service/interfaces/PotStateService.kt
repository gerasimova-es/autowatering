package com.home.autowatering.service.interfaces

import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter

interface PotStateService {
    fun find(filter: PotStateFilter): List<PotState>
    fun save(state: PotState): PotState
    fun last(pot: Pot): PotState?
}