package com.home.autowatering.repository

import com.home.autowatering.entity.PotStateData
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PotStateRepository : JpaRepository<PotStateData, Long>