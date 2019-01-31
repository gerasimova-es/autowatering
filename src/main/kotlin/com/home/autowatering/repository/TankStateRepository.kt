package com.home.autowatering.repository

import com.home.autowatering.model.database.JpaTankState
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.sql.Date

@Repository
interface TankStateRepository : JpaRepository<JpaTankState, Long> {
    fun findByDateBetween(from: Date, to: Date)
}