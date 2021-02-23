package com.home.autowatering.dao

import com.home.autowatering.model.settings.Watering

interface WateringDao {

    fun get(): Watering

    fun save(watering: Watering)
}