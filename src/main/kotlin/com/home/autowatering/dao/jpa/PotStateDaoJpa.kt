package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.business.filter.PotStateFilter
import java.time.ZonedDateTime


//@Repository
class PotStateDaoJpa(
//    private val dataSource: DataSource,
//    private val potRepository: PotRepository,
//    private val stateRepository: PotStateRepository
) : PotStateDao {

    override fun last(pot: Pot): PotState? {
        return null
//        val jpaPot = potRepository.findOneByCode(pot.code) ?: throw PotNotFoundException(pot.code)
//        val state = stateRepository.findFirstByPotOrderByDateDesc(jpaPot)
//        return if (state == null) null else JpaPotStateConverter.fromJpa(state)
    }

    override fun find(filter: PotStateFilter): List<PotState> {
        return arrayListOf()
//        val pot = Tables.POT
//        val state = Tables.POT_STATE
//
//        val condition = trueCondition()
//
//        //todo reactor query building
//        if (filter.pot.id != null) {
//            condition.and(state.POT_ID.eq(filter.pot.id))
//        } else {
//            condition.and(pot.CODE.eq(filter.pot.code))
//        }
//        if (filter.from != null) {
//            condition.and(state.DATE.greaterOrEqual(java.sql.Date(filter.from!!.time)))
//        }
//        if (filter.to != null) {
//            condition.and(state.DATE.lessOrEqual(java.sql.Date(filter.to!!.time)))
//        }
//
//        val data =
//            DSL.using(dataSource, SQLDialect.SQLITE) //todo use database manager
//                .select(
//                    pot.ID,
//                    pot.CODE,
//                    pot.NAME,
//                    pot.MIN_HUMIDITY,
//                    pot.CHECK_INTERVAL,
//                    pot.WATERING_DURATION,
//                    state.ID,
//                    state.DATE,
//                    state.HUMIDITY,
//                    state.WATERING
//                )
//                .from(state)
//                .join(pot).on(state.POT_ID.eq(pot.ID))
//                .where(condition)
//                .orderBy(state.DATE)
//                .fetch()
//
//        var filtered = data.map { jpa ->
//            JpaPotStateConverter.fromJpa(
//                JpaPotState(
//                    id = jpa[6] as Long,
//                    pot = JpaPot(
//                        id = jpa[0] as Long?,
//                        code = jpa[1] as String,
//                        name = jpa[2] as String?,
//                        minHumidity = jpa[3] as Int?,
//                        checkInterval = jpa[4] as Int?,
//                        wateringDuration = jpa[5] as Int?
//                    ),
//                    date = jpa[7] as Date,
//                    humidity = jpa[8] as Int,
//                    watering = jpa[9] as Boolean
//                )
//            )
//        }
//
//        if (filter.from != null) {
//            filtered = filtered.filter { s -> s.date.after(filter.from) || s.date == filter.from }
//        }
//
//        if (filter.to != null) {
//            filtered = filtered.filter { s -> s.date.before(filter.to) || s.date == filter.from }
//        }
//
//        return filtered

    }

    override fun save(state: PotState): PotState {
        return PotState(pot = Pot(code = "1"), date = ZonedDateTime.now(), humidity = 1)
//        try {
//            val jpaPot = potRepository.findOneByCode(state.pot.code)
//                ?: throw PotNotFoundException("pot not found by code = ${state.pot.code}")
//            var jpaState = JpaPotStateConverter.fromEntity(state)
//            jpaState.pot = jpaPot
//            jpaState = stateRepository.save(jpaState)
//            return JpaPotStateConverter.fromJpa(jpaState)
//        } catch (exc: PersistenceException) {
//            throw SavingException(exc)
//        }
    }

}