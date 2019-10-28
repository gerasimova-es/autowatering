package com.home.autowatering.dao.exposed

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.business.filter.PotStateFilter
import com.home.autowatering.model.database.PotStateTable
import com.home.autowatering.model.database.PotTable
import com.home.autowatering.model.database.converter.PotStateConverter
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
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
        //todo check slice type and limit
        return transaction {
            val query = (PotStateTable innerJoin PotTable).selectAll()

            if (filter.pot.id != null) query.adjustWhere {
                Op.build { PotStateTable.id eq filter.pot.id!! }
            }
            if (filter.from != null) query.adjustWhere {
                Op.build {
                    PotStateTable.date greaterEq DateTime(
                        filter.from!!.toInstant().toEpochMilli(),
                        DateTimeZone.forID(filter.from!!.zone.id)
                    )
                }
            }
            if (filter.to != null) query.adjustWhere {
                Op.build {
                    PotStateTable.date lessEq DateTime(
                        filter.to!!.toInstant().toEpochMilli(),
                        DateTimeZone.forID(filter.to!!.zone.id)
                    )
                }
            }
            query.orderBy(PotStateTable.date)
                .limit(filter.limit ?: 100)
                .toList()
                .map {
                    PotStateConverter.fromDB(it)
                }
        }
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