package com.home.autowatering.service

import com.home.autowatering.dao.WhistlingSettingsDao
import com.home.autowatering.model.WhistlingSettings
import org.springframework.stereotype.Service

@Service
class WhistleService(private var whistlingSettingsDao: WhistlingSettingsDao) {

    fun getSettings(): WhistlingSettings = whistlingSettingsDao.get()
}