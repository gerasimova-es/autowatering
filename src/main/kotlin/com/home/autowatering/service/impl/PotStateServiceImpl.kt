package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import org.springframework.stereotype.Service

@Service
class PotStateServiceImpl(private val stateDao: PotStateDao) : PotStateService {
    override fun find(filter: PotStateFilter): List<PotState> =
        stateDao.find(filter)
}