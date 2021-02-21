package com.home.autowatering.dao

import com.home.autowatering.model.WateringSettings

interface WateringSettingsDao {
    fun get(): WateringSettings
}