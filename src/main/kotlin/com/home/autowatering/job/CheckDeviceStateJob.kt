package com.home.autowatering.job

import com.home.autowatering.invoker.DeviceInvoker
import com.home.autowatering.service.ClimateService
import com.home.autowatering.service.LightingService
import com.home.autowatering.service.TankerService
import com.home.autowatering.service.VaporizeService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CheckDeviceStateJob(
    private var deviceInvoker: DeviceInvoker,
    private var climateService: ClimateService,
    private var tankerService: TankerService,
    private var vaporizeService: VaporizeService,
    private var lightingService: LightingService
) {

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(CheckDeviceStateJob::class.java)
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000)
    fun checkState() {
        LOGGER.info("checking state...")
        try {
            val state = deviceInvoker.getState()
            climateService.saveAirCondition(state.airCondition)
            climateService.saveGroundCondition(state.groundCondition)
            tankerService.saveHistory(state.tankerHistory)
            vaporizeService.saveHistory(state.vaporizerHistory)
            lightingService.saveHistory(state.lightingHistory)
            LOGGER.info("state checked successfully")
        } catch (e: Exception) {
            LOGGER.error("error during checking state: ", e)
        }
    }
}