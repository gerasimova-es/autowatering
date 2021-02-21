package com.home.autowatering.dao.repository

import com.home.autowatering.dao.jpa.entity.JpaLightingSettings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LightingSettingsRepository : JpaRepository<JpaLightingSettings, Long>