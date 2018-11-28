package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.model.Pot
import com.home.autowatering.model.TankState
import com.home.autowatering.model.WateringState
import com.home.autowatering.service.interfaces.WateringStateService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class WateringStateServiceImpl(
    private val potDao: PotDao,
    private val potStateDao: PotStateDao,
    private val tankStateDao: TankStateDao
) :
    WateringStateService {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(WateringStateServiceImpl::class.java)
    }

    override fun load(state: WateringState) {
        try {
            val pot = potDao.findByName(state.potName) ?: potDao.save(Pot(state.potName))
            potStateDao.save(pot, state.humidity)
            tankStateDao.save(TankState(state.tankName, Date(), state.tankVolume, state.tankVolume)) //todo set filled
        } catch (exc: Exception) {
            logger.error("watering state saving error:", exc)
            throw exc
        }
    }
}