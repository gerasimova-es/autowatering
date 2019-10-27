package com.home.autowatering.service.interfaces

import com.home.autowatering.model.business.Pot
import io.vertx.core.Future

interface WateringSystemService {
    fun refresh(pot: Pot, timeout: Long = 5000) : Future<Pot>
}