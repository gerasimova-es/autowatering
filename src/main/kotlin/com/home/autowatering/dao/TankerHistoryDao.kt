package com.home.autowatering.dao

import com.home.autowatering.model.history.TankerHistory

interface TankerHistoryDao {

    fun save(history: TankerHistory)
}