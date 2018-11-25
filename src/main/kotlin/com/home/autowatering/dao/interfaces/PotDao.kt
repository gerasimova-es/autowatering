package com.home.autowatering.dao.interfaces

interface PotDao {
    fun save(humidity: Double)
}