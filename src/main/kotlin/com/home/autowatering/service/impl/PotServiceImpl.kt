package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.filter.PotFilter
import com.home.autowatering.service.interfaces.PotService
import io.vertx.core.Future

class PotServiceImpl(
    private val potDao: PotDao
) : PotService {

    override fun findAll(): Future<List<Pot>> =
        Future.future<List<Pot>> {future ->
            future.complete(potDao.findAll())
        }

    override fun find(filter: PotFilter): Future<List<Pot>> =
        Future.future<List<Pot>> { future ->
            val pot: Pot? = when {
                filter.id != null -> potDao.findById(filter.id!!)
                filter.code != null -> potDao.findByCode(filter.code!!)
                else -> throw IllegalArgumentException("filter parameters are empty")
            }
            future.complete(if (pot == null) arrayListOf() else arrayListOf(pot))
        }

    override fun merge(source: Pot, target: Pot): Pot {
        return target.copy(
            name = source.name,
            minHumidity = source.minHumidity,
            checkInterval = source.checkInterval,
            wateringDuration = source.wateringDuration
        )
    }

    override fun save(pot: Pot): Future<Pot> =
        Future.future<Pot> {future ->
            future.complete(potDao.save(pot))
        }
}