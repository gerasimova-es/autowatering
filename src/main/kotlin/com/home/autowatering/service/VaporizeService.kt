package com.home.autowatering.service

import com.home.autowatering.dao.VaporiseDao
import com.home.autowatering.dao.VaporizeHistoryDao
import com.home.autowatering.model.history.VaporizerHistory
import com.home.autowatering.model.settings.Vaporizer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VaporizeService(
    private var vaporizeDao: VaporiseDao,
    private var vaporizerHistoryDao: VaporizeHistoryDao
) {

    @Transactional(readOnly = true)
    fun getSettings(): Vaporizer = vaporizeDao.get()

    @Transactional
    fun saveSettings(vaporizer: Vaporizer){
        var saved = vaporizeDao.get()
        saved.checkInterval = vaporizer.checkInterval
        saved.minHumidity = vaporizer.minHumidity
        saved.enabled = vaporizer.enabled
        vaporizeDao.save(vaporizer)
    }

    @Transactional
    fun saveHistory(history: VaporizerHistory) {
        vaporizerHistoryDao.save(history)
    }
}