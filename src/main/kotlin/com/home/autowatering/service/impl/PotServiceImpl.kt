package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.filter.PotFilter
import com.home.autowatering.service.interfaces.PotService

//@Service
class PotServiceImpl(val potDao: PotDao, val wateringSystemDao: WateringSystemDao) : PotService {

    override fun findAll(): List<Pot> =
        potDao.findAll()

    override fun find(filter: PotFilter): List<Pot> {
        val pot: Pot? = when {
            filter.id != null -> potDao.findById(filter.id!!)
            filter.code != null -> potDao.findByCode(filter.code!!)
            else -> throw IllegalArgumentException("filter parameters are empty")
        }
        return if (pot == null) arrayListOf() else arrayListOf(pot)
    }

    override fun merge(source: Pot, target: Pot): Pot {
        return target.copy(
            name = source.name,
            minHumidity = source.minHumidity,
            checkInterval = source.checkInterval,
            wateringDuration = source.wateringDuration
        )
    }

    override fun save(pot: Pot): Pot {
        return potDao.save(pot)
    }

}