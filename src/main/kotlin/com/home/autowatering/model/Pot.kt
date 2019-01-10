package com.home.autowatering.model

data class Pot(var id: Long? = null, val name: String, var states: List<PotState> = arrayListOf())