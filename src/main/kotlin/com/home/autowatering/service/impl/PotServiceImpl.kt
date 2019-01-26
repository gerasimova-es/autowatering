package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.model.Pot
import com.home.autowatering.model.filter.PotFilter
import com.home.autowatering.service.interfaces.PotService
import org.apache.commons.lang.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PotServiceImpl(@Autowired val potDao: PotDao) : PotService {

    override fun findAll(): List<Pot> =
        potDao.getAll()

    override fun find(filter: PotFilter): Pot? {
        Validate.notNull(filter)
        if (filter.id != null) {
            return potDao.getById(filter.id!!)
        } else if (filter.name != null) {
            return potDao.findByName(filter.name!!)
        }
        throw IllegalArgumentException("filter parameters are empty")
    }

    override fun merge(source: Pot, target: Pot): Pot {
        Validate.noNullElements(arrayOf(source, target))
        target.name = source.name
        target.description = source.description
        target.state = source.state
        return target
    }

    override fun save(pot: Pot): Pot =
        potDao.save(pot)

}