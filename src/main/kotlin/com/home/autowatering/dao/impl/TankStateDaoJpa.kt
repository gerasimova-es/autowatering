package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.entity.hibernate.TankStateData
import com.home.autowatering.model.TankState
import com.home.autowatering.repository.TankStateRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class TankStateDaoJpa(private val stateRepository: TankStateRepository) : TankStateDao {
    override fun save(tankState: TankState): TankState {

        val tankStateData: TankStateData = stateRepository.save(
            TankStateData(
                tankState.name,
                java.sql.Date(tankState.date.time),
                tankState.volume,
                tankState.filled
            )
        )

        return TankState(
            tankStateData.id,
            tankStateData.name!!,
            Date(tankStateData.date!!.time),
            tankStateData.volume,
            tankStateData.filled
        )
    }
}