package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.TankStateDao
import com.home.autowatering.model.business.TankState
import com.home.autowatering.dao.entity.JpaTankState
import com.home.autowatering.dao.repository.TankStateRepository
import org.springframework.stereotype.Repository

@Repository
class TankStateDaoJpa(private val stateRepository: TankStateRepository) :
    TankStateDao {
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