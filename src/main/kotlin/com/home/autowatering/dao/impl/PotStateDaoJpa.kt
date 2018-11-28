package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.entity.hibernate.PotStateData
import com.home.autowatering.entity.jooq.Tables.POT
import com.home.autowatering.entity.jooq.Tables.POT_STATE
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.repository.PotRepository
import com.home.autowatering.repository.PotStateRepository
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Repository
import java.sql.Date
import java.time.LocalDate
import java.util.function.Function
import javax.persistence.PersistenceException
import javax.sql.DataSource


@Repository
class PotStateDaoJpa(
    private val dataSource: DataSource,
    private val potRepository: PotRepository,
    private val stateRepository: PotStateRepository
) : PotStateDao {

    override fun find(filter: PotStateFilter): List<PotState> {
        val pot = POT
        val state = POT_STATE

        val condition = trueCondition()
            .and(filter.pot.id == null).or(pot.ID.eq(filter.pot.id))
            .and(filter.from == null).or(state.DATE.greaterOrEqual(java.sql.Date(filter.from!!.time)))
            .and(filter.to == null).or(state.DATE.lessOrEqual(java.sql.Date(filter.to!!.time)))

        val fetch = DSL.using(dataSource, SQLDialect.SQLITE)
            .select(state.ID, pot.ID, pot.NAME, state.DATE, state.HUMIDITY)
            .from(state)
            .join(pot).on(state.POT_ID.eq(pot.ID))
            .where(condition)
            .orderBy(state.DATE)
            .fetch()

        fetch.stream()
            .map(Function() {

            })


    }

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