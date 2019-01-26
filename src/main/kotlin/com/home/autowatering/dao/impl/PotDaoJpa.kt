package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.entity.hibernate.JpaPot
import com.home.autowatering.entity.hibernate.converter.JpaPotConverter
import com.home.autowatering.entity.hibernate.converter.JpaPotStateConverter
import com.home.autowatering.model.Pot
import com.home.autowatering.repository.PotRepository
import com.home.autowatering.repository.PotStateRepository
import org.springframework.stereotype.Repository
import java.util.stream.Collectors
import javax.transaction.Transactional

@Repository
class PotDaoJpa(private val potRepository: PotRepository, private val potStateRepository: PotStateRepository) : PotDao {
    val potConverter = JpaPotConverter()
    val stateConverter = JpaPotStateConverter()

    override fun getAll(): List<Pot> {
        return potRepository.findAll().stream()
            .map { jpaPot ->
                potConverter.fromJpa(
                    jpaPot,
                    potStateRepository.findFirstByPotOrderByDateDesc(jpaPot)
                )
            }
            .collect(Collectors.toList())
    }

    override fun getById(id: Long): Pot {
        val jpaPot = potRepository.getOne(id)
        return potConverter.fromJpa(
            jpaPot,
            potStateRepository.findFirstByPotOrderByDateDesc(jpaPot)
        )
    }

    override fun findByName(name: String): Pot? {
        val jpaPot: JpaPot? = potRepository.findOneByName(name)
        return if (jpaPot == null) null
        else potConverter.fromJpa(
            jpaPot,
            potStateRepository.findFirstByPotOrderByDateDesc(jpaPot)
        )
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
        if (pot.state == null) {
            return potConverter.fromJpa(jpaPot)
        }

        val jpaState = stateConverter.fromEntity(pot.state!!)
        jpaState.pot = jpaPot //todo хм
        return potConverter.fromJpa(jpaPot, potStateRepository.save(jpaState))
    }
}