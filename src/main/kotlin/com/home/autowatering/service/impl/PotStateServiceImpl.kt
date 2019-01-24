package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import org.apache.commons.lang.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PotStateServiceImpl(
    @Autowired private val potDao: PotDao,
    @Autowired private val stateDao: PotStateDao
) : PotStateService {
    override fun find(filter: PotStateFilter): List<PotState> {
        Validate.notNull(filter)
        return stateDao.find(filter)
    }

    override fun save(state: PotState): PotState {
        Validate.notNull(state)
        Validate.noNullElements(arrayOf(state.pot, state.date, state.humidity))
        state.pot = potDao.getByName(state.pot.name) ?: potDao.save(state.pot)
        return stateDao.save(state)
    }
}