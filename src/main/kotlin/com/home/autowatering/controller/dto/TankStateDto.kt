package com.home.autowatering.controller.dto

data class TankStateDto(
    val id: Long? = null,
    val name: String,
    val date: Long? = null,
    val volume: Double,
    val filled: Double
)