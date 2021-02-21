package com.home.autowatering.service

import com.home.autowatering.dao.VaporiseSettingsDao
import com.home.autowatering.model.VaporizeSettings
import org.springframework.stereotype.Service

@Service
class VaporizeService(private var vaporizeSettingsDao: VaporiseSettingsDao) {

    fun getSettings(): VaporizeSettings = vaporizeSettingsDao.get()
}