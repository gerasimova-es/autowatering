package com.home.autowatering.dao

import com.home.autowatering.model.settings.Lighting

interface LightingDao {

    fun get(): Lighting
}