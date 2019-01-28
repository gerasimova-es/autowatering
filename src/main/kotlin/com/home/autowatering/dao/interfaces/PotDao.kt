package com.home.autowatering.dao.interfaces

import com.home.autowatering.model.Pot

interface PotDao {
    /**
     * get all pots
     */
    fun findAll(): List<Pot>

    /**
     * Search pot by id
     * If pot is not found method will throw exception PotNotFoundException
     */
    fun findById(id: Long): Pot

    /**
     * Search pot by code
     * If pot is not found method method will return null
     */
    fun findByCode(code: String): Pot?

    /**
     * Saves pot
     * If pot does not exists with the save id it will be insert
     * or else it will be update
     */
    fun save(pot: Pot): Pot
}