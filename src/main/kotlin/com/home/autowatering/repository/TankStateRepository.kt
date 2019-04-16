package com.home.autowatering.repository

import java.sql.Date

//@Repository
interface TankStateRepository
//    : JpaRepository<JpaTankState, Long>
{
    fun findByDateBetween(from: Date, to: Date)
}