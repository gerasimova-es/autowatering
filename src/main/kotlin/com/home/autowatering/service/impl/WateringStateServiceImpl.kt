package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.model.Pot
import com.home.autowatering.model.TankState
import com.home.autowatering.model.WateringState
import com.home.autowatering.service.interfaces.WateringStateService
import org.apache.commons.lang.Validate
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
        validate(state)
        logger.info("saving watering state $state...")
        try {
            val pot = potDao.findByName(state.potName) ?: potDao.save(Pot(state.potName))
            logger.info("used pot [id=${pot.id}, name=${pot.name}")

            potStateDao.save(pot, state.potHumidity)
            logger.info("saved pot state [pot=$pot, humidity=${state.potHumidity}]")

            val tankState = TankState(state.tankName, Date(), state.tankVolume, state.tankVolume)
            tankStateDao.save(tankState)
            logger.info("saved tank state info [state=$tankState]")

        } catch (exc: Exception) {
            logger.error("watering state saving error:", exc)
            throw exc
        }
    }

    private fun validate(state: WateringState) {
        Validate.notNull(state)
        Validate.noNullElements(
            arrayOf(
                state.potName,
                state.potHumidity,
                state.tankName,
                state.tankVolume
            )
        )
        Validate.notEmpty(state.potName, "pot name cannot be empty")
        Validate.notEmpty(state.tankName, "tank name cannot be empty")

        if (state.potHumidity <= 0) {
            throw IllegalArgumentException("pot humidity cannot be less or equals zero")
        }
        if (state.tankVolume < 0) {
            throw IllegalArgumentException("tank volume cannot be less zero")
        }
    }

}