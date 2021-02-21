package com.home.autowatering.dao

import com.home.autowatering.model.WhistlingSettings

interface WhistlingSettingsDao {
    fun get(): WhistlingSettings
}