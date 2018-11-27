package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.entity.PotData
import com.home.autowatering.entity.PotStateData
import com.home.autowatering.repository.PotRepository
import com.home.autowatering.repository.PotStateRepository
import org.springframework.stereotype.Component

@Component
class PotStateStateDaoImpl(val stateRepository: PotStateRepository, val potRepostory: PotRepository) : PotStateDao {

    override fun save(potName: String, humidity: Double): PotStateData {
        var pot: PotData? = potRepostory.getOneByName(potName)
        if (pot == null) {
            pot = potRepostory.save(PotData(potName))
        }
        return stateRepository.save(PotStateData(pot!!, humidity))
    }
}