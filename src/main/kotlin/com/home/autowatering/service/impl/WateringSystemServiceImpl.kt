package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.service.interfaces.WateringSystemService
import io.vertx.core.Future
import io.vertx.core.Future.future
import org.apache.commons.lang.Validate

class WateringSystemServiceImpl(
    private val wateringSystemDao: WateringSystemDao
) : WateringSystemService {

    override fun refresh(pot: Pot, timeout: Long): Future<Pot> =
        future<Pot> { future ->
            Validate.noNullElements(
                arrayOf(pot.code, pot.minHumidity, pot.checkInterval, pot.wateringDuration)
            )
            future.complete()
        }.compose {
            wateringSystemDao.refresh(pot)
        }
}