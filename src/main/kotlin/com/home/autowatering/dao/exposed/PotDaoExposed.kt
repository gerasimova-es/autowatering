package com.home.autowatering.dao.exposed

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.database.save
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.database.PotTable
import com.home.autowatering.model.database.converter.PotConverter
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

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
                PotTable.save(
                    PotConverter.fromPojo(pot)
                )
            } else {
                PotTable.save(
                    PotConverter.map(pot, found)
                )
            }
        }
    }
}