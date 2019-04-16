package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.model.business.TankState
import com.home.autowatering.service.interfaces.TankStateService
import org.apache.commons.lang.Validate

//@Service
class TankStateServiceImpl(val tankStateDao: TankStateDao) : TankStateService {

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