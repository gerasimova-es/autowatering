package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.entity.PotStateData
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.repository.PotRepository
import com.home.autowatering.repository.PotStateRepository
import org.springframework.stereotype.Repository
import java.sql.Date
import java.time.LocalDate
import javax.persistence.PersistenceException

@Repository
class PotStateDaoJpa(val potRepository: PotRepository, val stateRepository: PotStateRepository) : PotStateDao {
    override fun find(filter: PotStateFilter): List<PotState> =
        arrayListOf()

    override fun save(pot: Pot, humidity: Double): PotStateData =
        try {
            stateRepository.save(
                PotStateData(
                    potRepository.getOne(pot.id!!),
                    Date.valueOf(LocalDate.now()),
                    humidity
                )
            )
        } catch (exc: PersistenceException) {
            throw SavingException(exc)
        }

}