package com.home.autowatering.service

import com.home.autowatering.dao.WhistlingDao
import com.home.autowatering.model.settings.Whistling
import org.springframework.stereotype.Service

@Service
class WhistleService(private var whistlingSettingsDao: WhistlingDao) {

    fun getSettings(): Whistling = whistlingSettingsDao.get()
}