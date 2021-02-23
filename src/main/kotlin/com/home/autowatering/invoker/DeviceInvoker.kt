package com.home.autowatering.invoker

import com.home.autowatering.model.history.DeviceState
import com.home.autowatering.model.settings.DeviceSettings

interface DeviceInvoker {

    fun getState(): DeviceState

    fun refreshSettings(settings: DeviceSettings)

    fun watering()
}