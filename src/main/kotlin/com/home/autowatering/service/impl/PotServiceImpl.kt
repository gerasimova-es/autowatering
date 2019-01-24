package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.model.Pot
import com.home.autowatering.service.interfaces.PotService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PotServiceImpl(@Autowired val potDao: PotDao) : PotService {
    override fun getByName(name: String): Pot =
        potDao.getByName(name)

    override fun getById(id: Long): Pot =
        potDao.getById(id)

    override fun getAll(): List<Pot> =
        potDao.getAll()
}