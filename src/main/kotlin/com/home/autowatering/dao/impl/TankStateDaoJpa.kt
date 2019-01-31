package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.model.business.TankState
import com.home.autowatering.model.database.JpaTankState
import com.home.autowatering.repository.TankStateRepository
import org.springframework.stereotype.Repository

@Repository
class TankStateDaoJpa(private val stateRepository: TankStateRepository) : TankStateDao {
    override fun save(tankState: TankState): TankState {
        val tankStateData: JpaTankState = stateRepository.save(
            JpaTankState(
                tankState.name,
                tankState.date,
                tankState.volume,
                tankState.filled
            )
        )

        return TankState(
            tankStateData.id,
            tankStateData.name!!,
            tankStateData.date!!,
            tankStateData.volume,
            tankStateData.filled
        )
    }
}