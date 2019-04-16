package com.home.autowatering.repository

import com.home.autowatering.model.database.JpaPot

//@Repository
interface PotRepository
//    : JpaRepository<JpaPot, Long>
{
    fun findOneByCode(code: String): JpaPot?
}