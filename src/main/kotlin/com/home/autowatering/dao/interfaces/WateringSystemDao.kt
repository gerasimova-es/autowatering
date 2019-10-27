package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.business.Pot
import io.vertx.core.Future

interface WateringSystemDao {
    fun refresh(pot: Pot): Future<Pot>
}