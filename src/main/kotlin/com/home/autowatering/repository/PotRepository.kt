package com.home.autowatering.repository

import com.home.autowatering.model.database.JpaPot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PotRepository : JpaRepository<JpaPot, Long> {
    fun findOneByCode(code: String): JpaPot?
}