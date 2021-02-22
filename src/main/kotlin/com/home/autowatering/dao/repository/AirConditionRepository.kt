package com.home.autowatering.dao.repository

import com.home.autowatering.dao.jpa.entity.JpaAirCondition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AirConditionRepository : JpaRepository<JpaAirCondition, Long>