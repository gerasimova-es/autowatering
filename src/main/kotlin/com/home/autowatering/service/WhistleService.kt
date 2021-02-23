package com.home.autowatering.service

import com.home.autowatering.dao.WhistlingDao
import com.home.autowatering.model.settings.Whistling
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WhistleService(private var whistlingDao: WhistlingDao) {

    @Transactional(readOnly = true)
    fun getSettings(): Whistling = whistlingDao.get()

    @Transactional
    fun saveSettings(whistling: Whistling) {
        val saved = whistlingDao.get()

        saved.duration = whistling.duration
        saved.enabled = whistling.enabled
        saved.startTime = whistling.startTime
        saved.stopTime = whistling.stopTime

        whistlingDao.save(saved)
    }
}