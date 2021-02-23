package com.home.autowatering.invoker.converter

import com.home.autowatering.api.converter.RequestConverter
import com.home.autowatering.invoker.dto.state.AirStateDto
import com.home.autowatering.invoker.dto.state.DeviceStateDto
import com.home.autowatering.invoker.dto.state.GroundStateDto
import com.home.autowatering.invoker.dto.state.LightStateDto
import com.home.autowatering.invoker.dto.state.TankerStateDto
import com.home.autowatering.invoker.dto.state.VaporizerStateDto
import com.home.autowatering.model.history.AirCondition
import com.home.autowatering.model.history.DeviceState
import com.home.autowatering.model.history.GroundCondition
import com.home.autowatering.model.history.LightingHistory
import com.home.autowatering.model.history.TankerHistory
import com.home.autowatering.model.history.VaporizerHistory

object DeviceStateDtoConverter : RequestConverter<DeviceStateDto, DeviceState>(
    {
        DeviceState(
            airCondition = AirCondition(
                it.air!!.humidity!!,
                it.air!!.temperature!!,
                it.air!!.lastCheck!!
            ),
            groundCondition = GroundCondition(
                it.ground!!.humidity!!,
                it.ground!!.lastCheck!!
            ),
            tankerHistory = TankerHistory(
                it.tanker!!.full!!,
                it.tanker!!.lastCheck!!
            ),
            lightingHistory = LightingHistory(
                it.light!!.status!!,
                it.light!!.lastCheck!!
            ),
            vaporizerHistory = VaporizerHistory(
                it.vaporizer!!.status!!,
                it.vaporizer!!.lastCheck!!
            )
        )
    },
    {
        DeviceStateDto(
            air = AirStateDto(
                it.airCondition.humidity,
                it.airCondition.temperature,
                it.airCondition.checkDate
            ),
            ground = GroundStateDto(
                it.groundCondition.humidity,
                it.groundCondition.checkDate
            ),
            tanker = TankerStateDto(
                it.tankerHistory.full,
                it.tankerHistory.checkDate
            ),
            light = LightStateDto(
                it.lightingHistory.status,
                it.lightingHistory.checkDate
            ),
            vaporizer = VaporizerStateDto(
                it.vaporizerHistory.status,
                it.vaporizerHistory.checkDate
            )
        )
    }
)
