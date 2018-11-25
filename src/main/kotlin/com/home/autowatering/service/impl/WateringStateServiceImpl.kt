package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.dao.interfaces.TankDao
import com.home.autowatering.model.WateringState
import com.home.autowatering.service.interfaces.WateringStateService
import org.springframework.stereotype.Component

@Component
class WateringStateServiceImpl(val potDao: PotDao, val tankDao: TankDao) : WateringStateService {

    override fun load(state: WateringState): WateringState {
        potDao.save(state.humidity)
        tankDao.save(state.tankVolume)

        return state //todo
    }
}