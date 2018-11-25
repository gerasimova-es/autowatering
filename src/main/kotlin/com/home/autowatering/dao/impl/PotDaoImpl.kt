package com.home.autowatering.dao.impl

import com.home.autowatering.dao.interfaces.PotDao
import org.springframework.stereotype.Component

@Component
class PotDaoImpl : PotDao {
    override fun save(humidity: Double) {
        println("humidity $humidity")
    }
}