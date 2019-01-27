package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import org.apache.commons.lang.Validate
import org.springframework.stereotype.Service

@Service
class PotStateServiceImpl(
    val stateDao: PotStateDao
) : PotStateService {
    override fun save(state: PotState): PotState {
        Validate.notNull(state)
        return stateDao.save(state)
    }

    override fun find(filter: PotStateFilter): List<PotState> {
        Validate.notNull(filter)
        return stateDao.find(filter)
    }
}