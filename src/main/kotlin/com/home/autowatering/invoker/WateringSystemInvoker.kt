package com.home.autowatering.invoker

import com.home.autowatering.invoker.state.DeviceStateDto

interface WateringSystemInvoker {

    fun getState(): DeviceStateDto
}