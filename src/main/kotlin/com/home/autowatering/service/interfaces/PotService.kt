package com.home.autowatering.service.interfaces

import com.home.autowatering.model.Pot

interface PotService {
    fun getAll(): List<Pot>
    fun getById(id: Long): Pot
    fun getByName(name: String): Pot
    fun save(pot: Pot): Pot
}