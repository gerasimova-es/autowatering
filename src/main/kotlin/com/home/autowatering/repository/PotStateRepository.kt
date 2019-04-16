package com.home.autowatering.repository

import com.home.autowatering.model.database.JpaPot
import com.home.autowatering.model.database.JpaPotState

//@Repository
interface PotStateRepository
//    : JpaRepository<JpaPotState, Long>
{
    fun findFirstByPotOrderByDateDesc(pot: JpaPot): JpaPotState?
}