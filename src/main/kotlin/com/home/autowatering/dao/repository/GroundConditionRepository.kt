package com.home.autowatering.dao.repository

import com.home.autowatering.dao.jpa.entity.JpaGroundCondition
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroundConditionRepository : JpaRepository<JpaGroundCondition, Long>