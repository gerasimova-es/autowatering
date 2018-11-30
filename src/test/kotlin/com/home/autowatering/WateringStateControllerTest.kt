package com.home.autowatering

import com.home.autowatering.controller.WateringStateController
import com.home.autowatering.service.interfaces.PotStateService
import com.home.autowatering.service.interfaces.WateringStateService
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test

class WateringStateControllerTest {
    private lateinit var potStateService: PotStateService
    private lateinit var wateringStateService: WateringStateService
    private lateinit var wateringStateController: WateringStateController

    @Before
    fun init() {
        potStateService = mock()
        wateringStateService = mock()
        wateringStateController = WateringStateController(wateringStateService, potStateService)
    }

    @Test(expected = IllegalArgumentException::class.java)
    fun sendStateWithEmptyPotName() {
        wateringStateController.sendState("", 0.0, "", 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun sendStateWithZeroPotHumidity() {
        wateringStateController.sendState("test", 0.0, "", 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun sendStateWithNegativePotHumidity() {
        wateringStateController.sendState("test", -7.0, "", 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun sendStateWithEmptyTankName() {
        wateringStateController.sendState("test", 7.0, "", 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun sendStateWithNegativeTankVolume() {
        wateringStateController.sendState("test", 7.0, "test", -8.0)
    }
    //todo

}