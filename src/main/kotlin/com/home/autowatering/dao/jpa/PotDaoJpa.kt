package com.home.autowatering.dao.jpa

import com.home.autowatering.dao.PotDao
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.business.Pot
import com.home.autowatering.dao.entity.JpaPot
import com.home.autowatering.dao.converter.JpaPotConverter
import com.home.autowatering.dao.repository.PotRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
class PotDaoJpa(private val repository: PotRepository) : PotDao {

    override fun findAll(): List<Pot> {
        return repository.findAll()
            .map { JpaPotConverter.fromJpa(it) }
    }

    override fun findById(id: Long): Pot =
        JpaPotConverter.fromJpa(
            repository.findById(id)
            .orElseThrow { PotNotFoundException(id) }
        )

    override fun findByCode(code: String): Pot? {
        val pot: JpaPot? = repository.findOneByCode(code)
        return if (pot == null) null else JpaPotConverter.fromJpa(pot)
    }

    @Transactional
    override fun save(pot: Pot): Pot {
        val jpaPot: JpaPot?
        if (pot.id != null) {
            jpaPot = repository.findById(pot.id!!)
                .orElseThrow { PotNotFoundException(pot.id!!) }
            JpaPotConverter.map(pot, jpaPot)
        } else {
            jpaPot = JpaPotConverter.fromEntity(pot)
        }
        return JpaPotConverter.fromJpa(repository.save(jpaPot!!))
    }
}