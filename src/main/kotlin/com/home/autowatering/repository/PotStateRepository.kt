package com.home.autowatering.repository

import com.home.autowatering.model.database.JpaPot
import com.home.autowatering.model.database.JpaPotState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PotStateRepository : JpaRepository<JpaPotState, Long> {
    fun findFirstByPotOrderByDateDesc(pot: JpaPot): JpaPotState?
}