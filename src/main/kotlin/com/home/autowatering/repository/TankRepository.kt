package com.home.autowatering.repository

import com.home.autowatering.entity.TankData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TankRepository : JpaRepository<TankData, Long>