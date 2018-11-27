package com.home.autowatering.repository

import com.home.autowatering.entity.PotData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PotRepository : JpaRepository<PotData, Long> {
    fun getOneByName(name: String): PotData?
}