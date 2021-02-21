package com.home.autowatering.service

import com.home.autowatering.dao.LightingSettingsDao
import com.home.autowatering.model.LightingSettings
import org.springframework.stereotype.Service

@Service
class LightingService(private var lightingSettingsDao: LightingSettingsDao) {

    fun getSettings(): LightingSettings = lightingSettingsDao.get()

}