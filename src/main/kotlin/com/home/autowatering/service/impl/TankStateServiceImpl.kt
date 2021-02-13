package com.home.autowatering.service.impl

import com.home.autowatering.dao.TankStateDao
import com.home.autowatering.model.business.TankState
import com.home.autowatering.service.TankStateService
import org.apache.commons.lang.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TankStateServiceImpl(@Autowired val tankStateDao: TankStateDao) :
    TankStateService {

    override fun save(state: TankState): TankState {
        Validate.noNullElements(
            arrayOf(
                state.date,
                state.name,
                state.volume,
                state.filled
            )
        )
        return tankStateDao.save(state)
    }
}