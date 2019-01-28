package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotFilter
import com.home.autowatering.service.interfaces.PotService
import org.apache.commons.lang.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PotServiceImpl(@Autowired val potDao: PotDao) : PotService {

    override fun findAll(): List<Pot> =
        potDao.findAll()

    override fun find(filter: PotFilter): Pot? {
        Validate.notNull(filter)
        if (filter.id != null) {
            return potDao.findById(filter.id!!)
        } else if (filter.code != null) {
            return potDao.findByCode(filter.code!!)
        }
        throw IllegalArgumentException("filter parameters are empty")
    }

    override fun mergeState(source: PotState, target: Pot): Pot {
        Validate.noNullElements(arrayOf(source, target))
        target.humidity = source.humidity
        return target
    }

    override fun merge(source: Pot, target: Pot): Pot {
        Validate.noNullElements(arrayOf(source, target))
        target.code = source.code
        target.name = source.name
        target.humidity = source.humidity
        return target
    }

    override fun save(pot: Pot): Pot =
        potDao.save(pot)

}