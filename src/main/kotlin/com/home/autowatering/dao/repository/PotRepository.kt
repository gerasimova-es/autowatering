package com.home.autowatering.dao.repository

import com.home.autowatering.dao.entity.JpaPot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PotRepository : JpaRepository<JpaPot, Long> {
    fun findOneByCode(code: String): JpaPot?
}