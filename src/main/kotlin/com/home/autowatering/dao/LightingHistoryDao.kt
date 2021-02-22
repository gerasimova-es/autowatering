package com.home.autowatering.dao

import com.home.autowatering.model.history.LightingHistory

interface LightingHistoryDao {

    fun save(history: LightingHistory)
}