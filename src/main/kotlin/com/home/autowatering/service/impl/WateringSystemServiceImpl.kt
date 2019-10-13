package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.service.interfaces.WateringSystemService
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.apache.commons.lang.Validate

class WateringSystemServiceImpl(
    private val wateringSystemDao: WateringSystemDao
) : WateringSystemService {
    override fun refresh(pot: Pot, timeout: Long) {
        Validate.noNullElements(
            arrayOf(
                pot.code,
                pot.minHumidity,
                pot.checkInterval,
                pot.wateringDuration
            )
        )

        runBlocking {
            withTimeout(timeout) {
                launch {
                    wateringSystemDao.refresh(pot)
                }
            }
        }
    }

}