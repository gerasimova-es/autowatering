package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.entity.hibernate.JpaPot
import com.home.autowatering.entity.hibernate.converter.JpaPotConverter
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.Pot
import com.home.autowatering.repository.PotRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
class PotDaoJpa(private val repository: PotRepository) : PotDao {
    private final val converter = JpaPotConverter()

    override fun findAll(): List<Pot> {
        return repository.findAll()
            .map { converter.fromJpa(it) }
    }

    override fun findById(id: Long): Pot =
        converter.fromJpa(repository.findById(id)
            .orElseThrow { PotNotFoundException(id) }
        )

    override fun findByCode(code: String): Pot? {
        val pot: JpaPot? = repository.findOneByCode(code)
        return if (pot == null) null else converter.fromJpa(pot)
    }

    @Transactional
    override fun save(pot: Pot): Pot {
        val jpaPot: JpaPot?
        if (pot.id != null) {
            jpaPot = repository.findById(pot.id!!)
                .orElseThrow { PotNotFoundException(pot.id!!) }
            converter.map(pot, jpaPot)
        } else {
            jpaPot = converter.fromEntity(pot)
        }
        return converter.fromJpa(repository.save(jpaPot!!))
    }
}