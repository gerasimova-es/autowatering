package com.home.autowatering.repository

import com.home.autowatering.entity.TankStateData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface TankStateRepository : JpaRepository<TankStateData, Long> {
    fun findByDateBetween(from: Date, to: Date)
}