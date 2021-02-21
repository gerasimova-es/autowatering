package com.home.autowatering.service

import com.home.autowatering.dao.WateringSettingsDao
import com.home.autowatering.model.WateringSettings
import org.springframework.stereotype.Service

@Service
class WateringService(private var wateringSettingsDao: WateringSettingsDao) {

    fun getSettings(): WateringSettings = wateringSettingsDao.get()
}