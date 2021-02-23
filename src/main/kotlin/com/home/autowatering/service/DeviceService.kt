package com.home.autowatering.service

import com.home.autowatering.dao.LightingDao
import com.home.autowatering.dao.VaporiseDao
import com.home.autowatering.dao.WateringDao
import com.home.autowatering.dao.WhistlingDao
import com.home.autowatering.invoker.rest.DeviceRest
import com.home.autowatering.model.history.DeviceState
import com.home.autowatering.model.settings.DeviceSettings
import org.springframework.stereotype.Service

@Service
class DeviceService(
    private val deviceRest: DeviceRest,
    private val wateringDao: WateringDao,
    private val lightingDao: LightingDao,
    private val vaporiseDao: VaporiseDao,
    private val whistlingDao: WhistlingDao
) {

    fun refresh() {
        deviceRest.refreshSettings(
            DeviceSettings(
                lightingDao.get(),
                wateringDao.get(),
                whistlingDao.get(),
                vaporiseDao.get()
            )
        )
    }

    fun getState(): DeviceState = deviceRest.getState()
}