package com.home.autowatering.model

class WateringState(
    var potName: String, var humidity: Double,
    var tankName: String, var tankVolume: Double
) {
    override fun toString(): String =
        "WateringState(potName=$potName, humidity=$humidity, tankVolume=$tankVolume)"
}