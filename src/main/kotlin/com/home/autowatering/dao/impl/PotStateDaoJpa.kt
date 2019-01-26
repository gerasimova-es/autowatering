package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.entiry.jooq.Tables
import com.home.autowatering.entity.hibernate.JpaPotState
import com.home.autowatering.entity.hibernate.converter.JpaPotStateConverter
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.repository.PotRepository
import com.home.autowatering.repository.PotStateRepository
import org.apache.commons.lang.Validate
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Repository
import java.sql.Date
import javax.sql.DataSource


@Repository
class PotStateDaoJpa(
    private val dataSource: DataSource,
    private val potRepository: PotRepository,
    private val stateRepository: PotStateRepository
) : PotStateDao {

    val converter = JpaPotStateConverter()

    override fun findLastState(pot: Pot): PotState? {
        val found = potRepository.findOneByName(pot.name)
        Validate.notNull(found)//todo PotNotFoundException
        val state = stateRepository.findFirstByPotOrderByDateDesc(found!!)
        return if (state == null) null else converter.fromJpa(state)
    }

    override fun find(filter: PotStateFilter): List<PotState> {
        val pot = Tables.POT
        val state = Tables.POT_STATE

        val condition = trueCondition()

        //todo reactor query building
        if (filter.pot?.id != null) {
            condition.and(state.POT_ID.eq(filter.pot!!.id))
        } else if (filter.pot?.name != null) {
            condition.and(pot.NAME.eq(filter.pot!!.name))
        }

        if (filter.from != null) {
            condition.and(state.DATE.greaterOrEqual(java.sql.Date(filter.from!!.time)))
        }

        if (filter.to != null) {
            condition.and(state.DATE.lessOrEqual(java.sql.Date(filter.to!!.time)))
        }

        val data =
            DSL.using(dataSource, SQLDialect.SQLITE) //todo use entity manager
                .select(state.ID, state.DATE, state.HUMIDITY)
                .from(state)
                .join(pot).on(state.POT_ID.eq(pot.ID))
                .where(condition)
                .orderBy(state.DATE)
                .fetch()

        return data.map { jpa ->
            converter.fromJpa(JpaPotState(id = jpa[0] as Long, date = jpa[1] as Date, humidity = jpa[2] as Double))
        }

    }

//    override fun save(state: PotState): PotState {
//        try {
//            val pot = potRepository.getOneByName(state.pot.name)
//                ?: throw PotNotFoundException("pot not found by name = ${state.pot.name}")
//            val saved = stateRepository.save(
//                JpaPotState(
//                    pot,
//                    java.sql.Date(state.date.time),
//                    state.humidity
//                )
//            )
//            //todo to JpaConverter
//            return PotState(
//                id = saved.id,
//                pot = Pot(pot.id!!, pot.name!!),
//                date = Date(saved.date!!.time),
//                humidity = saved.humidity!!
//            )
//        } catch (exc: PersistenceException) {
//            throw SavingException(exc)
//        }
//    }

}