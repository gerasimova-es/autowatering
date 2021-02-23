package com.home.autowatering.service

import com.home.autowatering.dao.WhistlingDao
import com.home.autowatering.model.settings.Whistling
import org.springframework.stereotype.Service

@Service
class WhistleService(private var whistlingDao: WhistlingDao) {

    fun getSettings(): Whistling = whistlingDao.get()

    fun saveSettings(whistling: Whistling) {
        val saved = whistlingDao.get()

        saved.duration = whistling.duration
        saved.enabled = whistling.enabled
        saved.startTime = whistling.startTime
        saved.stopTime = whistling.stopTime

        whistlingDao.save(saved)
    }
}