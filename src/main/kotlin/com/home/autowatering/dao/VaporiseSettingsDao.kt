package com.home.autowatering.dao

import com.home.autowatering.model.VaporizeSettings

interface VaporiseSettingsDao {

    fun get(): VaporizeSettings
}