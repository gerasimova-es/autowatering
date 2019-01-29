package com.home.autowatering.service.interfaces

import com.home.autowatering.model.Pot
import com.home.autowatering.model.filter.PotFilter

interface PotService {
    fun findAll(): List<Pot>
    fun find(filter: PotFilter): List<Pot>
    fun save(pot: Pot): Pot
    fun merge(source: Pot, target: Pot): Pot
}