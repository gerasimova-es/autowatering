package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.Pot

interface PotDao {
    fun findAll(): List<Pot>
    fun findById(id: Long): Pot
    fun findByCode(code: String): Pot?
    fun save(pot: Pot): Pot
}