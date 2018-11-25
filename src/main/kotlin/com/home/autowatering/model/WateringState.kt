package com.home.autowatering.model

data class WateringState(var humidity: Double, var tankVolume: Double) {
    override fun toString(): String {
        return "WateringState(humidity=$humidity, tankVolume=$tankVolume)"
    }
}