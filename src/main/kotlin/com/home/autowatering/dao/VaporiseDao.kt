package com.home.autowatering.dao

import com.home.autowatering.model.settings.Vaporizer

interface VaporiseDao {

    fun get(): Vaporizer

    fun save(vaporizer: Vaporizer)
}