package com.home.autowatering.service

import com.home.autowatering.dao.LightingDao
import com.home.autowatering.dao.LightingHistoryDao
import com.home.autowatering.model.history.LightingHistory
import com.home.autowatering.model.settings.Lighting
import org.springframework.stereotype.Service

@Service
class LightingService(
    private var lightingDao: LightingDao,
    private var lightingHistoryDao: LightingHistoryDao
) {

    fun getSettings(): Lighting = lightingDao.get()

    fun saveSettings(lighting: Lighting){
        val saved = lightingDao.get()

        saved.enabled = lighting.enabled
        saved.startTime = lighting.startTime
        saved.stopTime = lighting.stopTime

        lightingDao.save(saved)
    }

    fun saveHistory(history: LightingHistory) {
        lightingHistoryDao.save(history)
    }

}