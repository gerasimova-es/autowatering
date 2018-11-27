package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.dao.interfaces.TankDao
import com.home.autowatering.model.WateringState
import com.home.autowatering.service.interfaces.WateringStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class WateringStateServiceImpl(val potStateDao: PotStateDao, val tankDao: TankDao) : WateringStateService {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(WateringStateServiceImpl::class.java)
    }

    override fun load(state: WateringState): WateringState {
        try {
            potStateDao.save(state.potName, state.humidity)
            tankDao.save(state.tankVolume)
        } catch (exc: Exception) {
            logger.error("watering state saving error: ", exc)
            throw exc
        }

        return state //todo
    }
}