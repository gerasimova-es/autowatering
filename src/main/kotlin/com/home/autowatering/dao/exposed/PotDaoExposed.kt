package com.home.autowatering.dao.exposed

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.database.PotTable
import com.home.autowatering.model.database.converter.PotConverter
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PotDaoExposed : PotDao {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(PotDaoExposed::class.java)
    }
    override fun findAll(): List<Pot> =
        transaction {
            PotTable.selectAll()
                .map { PotConverter.fromJpa(it) }
        }

    override fun findById(id: Long): Pot =
        transaction {
            PotTable.select {
                PotTable.id eq id
            }.singleOrNull()
        }?.let { PotConverter.fromJpa(it) }
            ?: throw PotNotFoundException(id)


    override fun findByCode(code: String): Pot? =
        transaction {
            PotTable.select {
                PotTable.code eq code
            }.singleOrNull()
        }?.let { PotConverter.fromJpa(it) }

    //    @Transactional
    override fun save(pot: Pot): Pot {
        return Pot(code = "1")
//        val jpaPot: JpaPot?
//        if (pot.id != null) {
//            jpaPot = repository.findById(pot.id!!)
//                .orElseThrow { PotNotFoundException(pot.id!!) }
//            JpaPotConverter.map(pot, jpaPot)
//        } else {
//            jpaPot = JpaPotConverter.fromEntity(pot)
//        }
//        return JpaPotConverter.fromJpa(repository.save(jpaPot!!))
    }
}