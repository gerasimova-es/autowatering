package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.entity.hibernate.JpaPot
import com.home.autowatering.entity.hibernate.converter.JpaPotConverter
import com.home.autowatering.model.Pot
import com.home.autowatering.repository.PotRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
class PotDaoJpa(private val potRepository: PotRepository) : PotDao {
    val potConverter = JpaPotConverter()

    override fun getAll(): List<Pot> {
        return potRepository.findAll()
            .map { potConverter.fromJpa(it) }
    }

    override fun getById(id: Long): Pot =
        potConverter.fromJpa(potRepository.getOne(id))


    override fun findByCode(code: String): Pot? {
        val pot: JpaPot? = potRepository.findOneByCode(code)
        return if (pot == null) null else potConverter.fromJpa(pot)
    }

    @Transactional
    override fun save(pot: Pot): Pot {
        var jpaPot: JpaPot
        if (pot.id != null) {
            jpaPot = potRepository.getOne(pot.id!!)
            jpaPot = potRepository.save(potConverter.map(pot, jpaPot))
        } else {
            jpaPot = potRepository.save(potConverter.fromEntity(pot))
        }
        return potConverter.fromJpa(jpaPot)
    }
}