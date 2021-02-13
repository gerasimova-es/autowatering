package com.home.autowatering.service.impl

import com.home.autowatering.dao.WateringSystemDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.service.WateringSystemService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.commons.lang3.Validate
import org.springframework.stereotype.Service

@Service
class WateringSystemServiceImpl(val wateringSystemDao: WateringSystemDao) :
    WateringSystemService {
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