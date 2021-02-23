package com.home.autowatering.service

import com.home.autowatering.dao.TankerHistoryDao
import com.home.autowatering.model.history.TankerHistory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TankerService (private var tankerHistoryDao: TankerHistoryDao){

    @Transactional
    fun saveHistory(tankerHistory: TankerHistory){
        tankerHistoryDao.save(tankerHistory)
    }
}