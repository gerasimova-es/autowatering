package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.entity.TankStateData
import com.home.autowatering.model.TankState
import com.home.autowatering.repository.TankStateRepository
import org.springframework.stereotype.Repository

@Repository
class TankStateStateDaoJpa(val stateRepository: TankStateRepository) : TankStateDao {
    override fun save(tankState: TankState): TankState {
        val tankStateData: TankStateData =
            stateRepository.save(TankStateData(tankState.name, tankState.volume, tankState.filled))
        return TankState(tankStateData.id, tankStateData.name!!, tankStateData.volume, tankStateData.filled)
    }
}