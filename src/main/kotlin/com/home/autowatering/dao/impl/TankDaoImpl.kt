package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.TankDao
import org.springframework.stereotype.Component

@Component
class TankDaoImpl : TankDao {
    //todo
    override fun save(volume: Double) {
        println("Saving pot volume $volume")
    }
}