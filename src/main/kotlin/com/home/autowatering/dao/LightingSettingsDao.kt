package com.home.autowatering.dao

import com.home.autowatering.model.LightingSettings

interface LightingSettingsDao {
    fun get(): LightingSettings
}