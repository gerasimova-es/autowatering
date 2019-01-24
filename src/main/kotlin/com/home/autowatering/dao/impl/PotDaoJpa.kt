package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.entity.hibernate.PotData
import com.home.autowatering.model.Pot
import com.home.autowatering.repository.PotRepository
import org.springframework.stereotype.Repository
import java.util.stream.Collectors

@Repository
class PotDaoJpa(private val potRepository: PotRepository) : PotDao {
    override fun getAll(): List<Pot> {
        return potRepository.findAll().stream()
            .map { pot -> Pot(pot.id!!, pot.name!!) }
            .collect(Collectors.toList())
    }

    override fun getById(id: Long): Pot {
        val pot = potRepository.getOne(id)
        return Pot(pot.id!!, pot.name!!)
    }

    override fun getByName(name: String): Pot {
        val pot: PotData = potRepository.getOneByName(name)
        return Pot(pot.id!!, pot.name!!)
    }

    override fun save(pot: Pot): Pot {
        val potData: PotData = potRepository.save(
            PotData(
                pot.name
            )
        )
        return Pot(potData.id!!, potData.name!!)
    }

}