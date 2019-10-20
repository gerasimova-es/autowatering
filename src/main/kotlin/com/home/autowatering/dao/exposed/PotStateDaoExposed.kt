package com.home.autowatering.dao.exposed

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.business.filter.PotStateFilter
import com.home.autowatering.model.database.PotStateTable
import com.home.autowatering.model.database.PotTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.DateTimeZone


class PotStateDaoExposed : PotStateDao {

    override fun last(pot: Pot): PotState? {
        transaction {
            val foundPot = PotTable.select { PotTable.code eq pot.code }.singleOrNull()
                ?: throw PotNotFoundException("pot not found by code = ${pot.code}")

//            val state = PotStateTable
//                .slice(PotStateTable.pot, , maxColumn)
//                .select {
//                PotStateTable.pot eq foundPot[PotTable.id]
//            }.
//
//
//                .selectAll()
//                .groupBy(idColumn)
//                .orderBy(maxColumn, SortOrder.DESC)

            //        val state = stateRepository.findFirstByPotOrderByDateDesc(jpaPot)
//        return if (state == null) null else JpaPotStateConverter.fromJpa(state)
        }


        return null
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

    override fun save(state: PotState): PotState =
        transaction {
            kotlin.runCatching {
                val foundPot = PotTable.select { PotTable.code eq state.pot.code }.singleOrNull()
                    ?: throw PotNotFoundException("pot not found by code = ${state.pot.code}")

                val id = PotStateTable.insert {
                    it[date] = DateTime(
                        state.date.toInstant().toEpochMilli(),
                        DateTimeZone.forID(state.date.zone.id)
                    )
                    it[pot] = foundPot[PotTable.id]
                    it[humidity] = state.humidity
                    it[watering] = state.watering!!
                }
                state.id = id.generatedKey?.toLong()

                //todo return converted from DB pot
                state
            }.onFailure {
                throw SavingException(it)
            }.getOrThrow()
        }
}