package com.home.autowatering.service

import com.home.autowatering.dao.LightingDao
import com.home.autowatering.dao.LightingHistoryDao
import com.home.autowatering.model.history.LightingHistory
import com.home.autowatering.model.settings.Lighting
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LightingService(
    private var lightingDao: LightingDao,
    private var lightingHistoryDao: LightingHistoryDao
) {

    @Transactional(readOnly = true)
    fun getSettings(): Lighting = lightingDao.get()

    @Transactional
    fun saveSettings(lighting: Lighting){
        val saved = lightingDao.get()

        saved.enabled = lighting.enabled
        saved.startTime = lighting.startTime
        saved.stopTime = lighting.stopTime

        lightingDao.save(saved)
    }

    @Transactional
    fun saveHistory(history: LightingHistory) {
        lightingHistoryDao.save(history)
    }

}