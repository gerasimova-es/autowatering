package com.home.autowatering.model

data class WateringState(var potName: String, var humidity: Double, var tankVolume: Double) {
    override fun toString(): String {
        return "WateringState(potName=$potName, humidity=$humidity, tankVolume=$tankVolume)"
    }
}