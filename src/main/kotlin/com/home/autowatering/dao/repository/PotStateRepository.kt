package com.home.autowatering.dao.repository

import com.home.autowatering.dao.entity.JpaPot
import com.home.autowatering.dao.entity.JpaPotState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PotStateRepository : JpaRepository<JpaPotState, Long> {
    fun findFirstByPotOrderByDateDesc(pot: JpaPot): JpaPotState?
}