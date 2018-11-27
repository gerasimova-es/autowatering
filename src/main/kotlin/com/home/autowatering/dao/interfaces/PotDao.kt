package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.Pot

interface PotDao {
    fun save(pot: Pot): Pot
    fun findByName(name: String): Pot?
}