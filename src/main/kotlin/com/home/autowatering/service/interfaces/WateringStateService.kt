package com.home.autowatering.service.interfaces

import com.home.autowatering.model.WateringState

interface WateringStateService {
    fun load(state: WateringState): WateringState
}