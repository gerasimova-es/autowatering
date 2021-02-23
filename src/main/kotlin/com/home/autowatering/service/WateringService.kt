package com.home.autowatering.service

import com.home.autowatering.dao.WateringDao
import com.home.autowatering.dao.jpa.converter.JpaWateringConverter
import com.home.autowatering.invoker.DeviceInvoker
import com.home.autowatering.model.settings.Watering
import org.springframework.stereotype.Service

@Service
class WateringService(
    private var wateringDao: WateringDao,
    private var deviceInvoker: DeviceInvoker
) {

    fun getSettings(): Watering = wateringDao.get()

    fun saveSettings(watering: Watering) {
        val saved = wateringDao.get()

        saved.minHumidity = watering.minHumidity
        saved.duration = watering.duration
        saved.checkInterval = watering.checkInterval
        saved.enabled = watering.enabled

        wateringDao.save(saved)
    }

    fun watering() = deviceInvoker.watering()
}