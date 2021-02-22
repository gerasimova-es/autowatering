package com.home.autowatering.model.history

data class DeviceState(
    val airCondition: AirCondition,
    val groundCondition: GroundCondition,
    val tankerHistory: TankerHistory,
    val lightingHistory: LightingHistory,
    val vaporizerHistory: VaporizerHistory
)