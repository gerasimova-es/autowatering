package com.home.autowatering.repository

import com.home.autowatering.model.database.PotStateTable
import com.home.autowatering.model.database.PotTable

//@Repository
interface PotStateRepository
//    : JpaRepository<JpaPotState, Long>
{
    fun findFirstByPotOrderByDateDesc(pot: PotTable): PotStateTable?
}