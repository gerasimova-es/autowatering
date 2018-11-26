package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.TankDao
import com.home.autowatering.entity.TankData
import com.home.autowatering.repository.TankRepository
import org.springframework.stereotype.Component

@Component
class TankDaoImpl(val repository: TankRepository) : TankDao {

    override fun save(volume: Double): TankData =
        repository.save(TankData(volume))
}