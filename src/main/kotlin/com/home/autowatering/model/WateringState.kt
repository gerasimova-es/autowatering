package com.home.autowatering.model

data class WateringState(
    var potName: String, var potHumidity: Double,
    var tankName: String, var tankVolume: Double
) {
    override fun toString(): String =
        "WateringState(potName=$potName, potHumidity=$potHumidity, tankName= $tankName, tankVolume=$tankVolume)"
}