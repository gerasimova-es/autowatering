package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.business.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService

//@Service
class PotStateServiceImpl(
    val stateDao: PotStateDao
) : PotStateService {

    override fun last(pot: Pot): PotState? =
        stateDao.last(pot)

    override fun save(state: PotState): PotState =
        stateDao.save(state)

    override fun find(filter: PotStateFilter): List<PotState> =
        stateDao.find(filter)
}