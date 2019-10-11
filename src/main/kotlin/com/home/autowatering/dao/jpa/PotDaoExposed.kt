package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.database.PotTable
import com.home.autowatering.model.database.converter.PotConverter
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PotDaoExposed : PotDao {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(PotDaoExposed::class.java)
    }

    override fun findAll(): List<Pot> =
        try {
            transaction {
                PotTable.selectAll()
                    .map { PotConverter.fromJpa(it) }
            }
        } catch (exc: Exception) {
            LOGGER.error("error ", exc)
            throw exc
        }

    override fun findById(id: Long): Pot =
        Pot(code = "1")
//        JpaPotConverter.fromJpa(
//            repository.findById(id)
//            .orElseThrow { PotNotFoundException(id) }
//        )

    override fun findByCode(code: String): Pot? {
        return null
//        val pot: JpaPot? = repository.findOneByCode(code)
//        return if (pot == null) null else JpaPotConverter.fromJpa(pot)
    }

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