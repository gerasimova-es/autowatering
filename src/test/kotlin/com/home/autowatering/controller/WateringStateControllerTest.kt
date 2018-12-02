package com.home.autowatering.controller

import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.service.interfaces.WateringStateService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify

class WateringStateControllerTest {
    private lateinit var wateringStateService: WateringStateService
    private lateinit var wateringStateController: WateringStateController

    @Before
    fun init() {
        wateringStateService = mock()
        wateringStateController = WateringStateController(wateringStateService)
    }


    @Test
    fun loadWithError() {
        whenever(wateringStateService.load(any())).thenThrow(RuntimeException())
        val result = wateringStateController.sendState("test", 1.0, "test", 1.0)

        verify(wateringStateService, times(1)).load(any())
        assertNotNull(result)
        assertEquals(ResponseStatus.ERROR, result.status)
    }

    @Test
    fun success() {
        val result = wateringStateController.sendState("test", 1.0, "test", 1.0)

        verify(wateringStateService, times(1)).load(any())
        assertNotNull(result)
        assertEquals(ResponseStatus.SUCCESS, result.status)
    }

}