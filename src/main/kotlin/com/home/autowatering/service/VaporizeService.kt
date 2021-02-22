package com.home.autowatering.service

import com.home.autowatering.dao.VaporiseDao
import com.home.autowatering.dao.VaporizeHistoryDao
import com.home.autowatering.model.history.VaporizerHistory
import com.home.autowatering.model.settings.Vaporizer
import org.springframework.stereotype.Service

@Service
class VaporizeService(
    private var vaporizeDao: VaporiseDao,
    private var vaporizerHistoryDao: VaporizeHistoryDao
) {

    fun getSettings(): Vaporizer = vaporizeDao.get()

    fun saveHistory(history: VaporizerHistory) {
        vaporizerHistoryDao.save(history)
    }
}