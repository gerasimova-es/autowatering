package com.home.autowatering.dao.exposed

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.database.PotTable
import com.home.autowatering.model.database.converter.PotConverter
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class PotDaoExposed : PotDao {

    override fun findAll(): List<Pot> =
        transaction {
            PotTable.selectAll()
                .map { PotConverter.fromDB(it) }
        }

    override fun findById(id: Long): Pot =
        transaction {
            PotTable.select {
                PotTable.id eq id
            }.singleOrNull()
        }?.let { PotConverter.fromDB(it) }
            ?: throw PotNotFoundException(id)

    override fun findByCode(code: String): Pot? =
        transaction {
            PotTable.select {
                PotTable.code eq code
            }.singleOrNull()
        }?.let { PotConverter.fromDB(it) }

    override fun save(pot: Pot): Pot {
        transaction {
            val found = pot.id?.let { id ->
                PotTable.select { PotTable.id eq id }
                    .single()
            }
            if (found == null) {
                val id = PotTable.insert {
                    it[code] = pot.code
                    it[name] = pot.name!!
                    it[minHumidity] = pot.minHumidity!!
                    it[checkInterval] = pot.checkInterval!!
                    it[wateringDuration] = pot.wateringDuration!!
                }
                pot.id = id.generatedKey?.toLong()
            } else {
                PotTable.update({ PotTable.id eq pot.id!! }) {
                    it[code] = pot.code
                    it[name] = pot.name!!
                    it[minHumidity] = pot.minHumidity!!
                    it[checkInterval] = pot.checkInterval!!
                    it[wateringDuration] = pot.wateringDuration!!
                }
            }
        }
        //todo return converted from DB pot
        return pot
    }
}