package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import org.springframework.stereotype.Service

@Service
class PotStateServiceImpl(
    val stateDao: PotStateDao
) : PotStateService {

    override fun last(pot: Pot): PotState? =
        stateDao.last(pot)


    override fun save(state: PotState): PotState {
        return stateDao.save(state)
    }

    override fun find(filter: PotStateFilter): List<PotState> {
        return stateDao.find(filter)
    }

}