package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.dao.rest.WateringSystemRest
import com.home.autowatering.model.business.Pot
import com.home.autowatering.service.interfaces.WateringSystemService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.lang3.Validate

//@Service
class WateringSystemServiceImpl(
    private val wateringSystemDao: WateringSystemDao = WateringSystemRest()
) : WateringSystemService {
    override fun refresh(pot: Pot) {
        Validate.noNullElements(
            arrayOf(
                pot.code,
                pot.minHumidity,
                pot.checkInterval,
                pot.wateringDuration
            )
        )
        //todo delete coroutines
        val job = GlobalScope.launch {
            wateringSystemDao.refresh(pot)
        }
    }

}