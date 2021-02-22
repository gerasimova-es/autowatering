package com.home.autowatering.service

import com.home.autowatering.dao.WateringDao
import com.home.autowatering.model.settings.Watering
import org.springframework.stereotype.Service

@Service
class WateringService(private var wateringSettingsDao: WateringDao) {

    fun getSettings(): Watering = wateringSettingsDao.get()
}