package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.filter.PotFilter
import com.home.autowatering.service.interfaces.PotService
import org.springframework.stereotype.Service

@Service
class PotServiceImpl(val potDao: PotDao) : PotService {

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
        target.code = source.code
        target.name = source.name
        return target
    }

    override fun save(pot: Pot): Pot =
        potDao.save(pot)

}