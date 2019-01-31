package com.home.autowatering.service.interfaces

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.business.filter.PotStateFilter

interface PotStateService {
    fun find(filter: PotStateFilter): List<PotState>
    fun save(state: PotState): PotState
    fun last(pot: Pot): PotState?
}