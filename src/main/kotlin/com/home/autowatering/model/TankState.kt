package com.home.autowatering.model

data class TankState(var id: Long?, var name: String, var volume: Double, var filled: Double) {
    constructor(name: String, volume: Double, filled: Double) : this(null, name, volume, filled)
}