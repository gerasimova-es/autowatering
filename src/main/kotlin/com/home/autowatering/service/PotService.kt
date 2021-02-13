package com.home.autowatering.service

import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.filter.PotFilter

interface PotService {
    fun findAll(): List<Pot>
    fun find(filter: PotFilter): List<Pot>
    fun save(pot: Pot): Pot
    fun merge(source: Pot, target: Pot): Pot
}