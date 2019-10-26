package com.home.autowatering.service.interfaces

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.filter.PotFilter
import io.vertx.core.Future

interface PotService {
    fun findAll(): Future<List<Pot>>
    fun find(filter: PotFilter): Future<List<Pot>>
    fun save(pot: Pot): Pot
    fun merge(source: Pot, target: Pot): Pot
}