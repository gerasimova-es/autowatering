package com.home.autowatering.invoker

import com.home.autowatering.model.history.DeviceState
import com.home.autowatering.model.settings.Lighting
import com.home.autowatering.model.settings.Vaporizer
import com.home.autowatering.model.settings.Watering
import com.home.autowatering.model.settings.Whistling

interface DeviceInvoker {

    fun getState(): DeviceState

    fun refreshSettings(
        lighting: Lighting,
        watering: Watering,
        whistling: Whistling,
        vaporizer: Vaporizer
    )
}