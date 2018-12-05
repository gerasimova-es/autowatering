package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import org.apache.commons.lang.Validate
import org.springframework.stereotype.Service

@Service
class PotStateServiceImpl(private val stateDao: PotStateDao) : PotStateService {
    override fun save(state: PotState): PotState {
        Validate.notNull(state)
        Validate.noNullElements(arrayOf(state.date, state.pot, state.pot, state.humidity))
        return stateDao.save(state)

    }

    override fun find(filter: PotStateFilter): List<PotState> =
        stateDao.find(filter)
}