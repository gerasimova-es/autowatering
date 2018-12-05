package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.model.TankState
import com.home.autowatering.service.interfaces.TankStateService
import org.apache.commons.lang.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TankStateServiceImpl : TankStateService {
    @Autowired
    lateinit var tankStateDao: TankStateDao

    override fun save(state: TankState): TankState {
        Validate.notNull(state)
        Validate.noNullElements(arrayOf(state.date, state.name, state.volume, state.filled))
        return tankStateDao.save(state)
    }
}