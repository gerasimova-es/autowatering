package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.entity.PotData
import com.home.autowatering.entity.PotStateData
import com.home.autowatering.repository.PotStateRepository
import org.springframework.stereotype.Component

@Component
class PotDaoImpl(val repository: PotStateRepository) : PotDao {

    override fun save(potName: String, humidity: Double): PotStateData =
        repository.save(PotStateData(PotData(potName), humidity))
}