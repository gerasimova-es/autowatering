package com.home.autowatering.dao

import com.home.autowatering.model.settings.Whistling

interface WhistlingDao {

    fun get(): Whistling
}