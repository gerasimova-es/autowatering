package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.Pot

interface PotDao {
    fun getAll(): List<Pot>
    fun getById(id: Long): Pot
    fun findByCode(code: String): Pot?
    fun save(pot: Pot): Pot
}