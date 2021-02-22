package com.home.autowatering.dao

import com.home.autowatering.model.history.VaporizerHistory

interface VaporizeHistoryDao {

    fun save(history: VaporizerHistory)
}