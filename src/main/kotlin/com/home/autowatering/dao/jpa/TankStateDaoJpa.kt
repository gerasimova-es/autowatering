package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.model.business.TankState
import com.home.autowatering.repository.TankStateRepository

//@Repository
class TankStateDaoJpa(private val stateRepository: TankStateRepository) : TankStateDao {
    override fun save(tankState: TankState): TankState {
        return TankState(name = "1", date = java.util.Date(), volume = 1.0, filled = 1.0)
//        val tankStateData: JpaTankState = stateRepository.save(
//            JpaTankState(
//                tankState.name,
//                tankState.date,
//                tankState.volume,
//                tankState.filled
//            )
//        )
//
//        return TankState(
//            tankStateData.id,
//            tankStateData.name!!,
//            tankStateData.date!!,
//            tankStateData.volume,
//            tankStateData.filled
//        )
    }
}