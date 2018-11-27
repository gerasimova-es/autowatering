package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.entity.PotData
import com.home.autowatering.model.Pot
import com.home.autowatering.repository.PotRepository
import org.springframework.stereotype.Component

@Component
class PotDaoJpa(val potRepository: PotRepository) : PotDao {
    override fun save(pot: Pot): Pot {
        val potData: PotData = potRepository.save(PotData(pot.name!!))
        return Pot(potData.id!!, potData.name!!)
    }

    override fun findByName(name: String): Pot? {
        val potData: PotData = potRepository.getOneByName(name) ?: return null
        return Pot(potData.id!!, potData.name!!)
    }
}