package com.home.autowatering.controller

import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.service.interfaces.PotStateService
import com.home.autowatering.service.interfaces.TankStateService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class WateringStateControllerTest {
    private lateinit var potStateService: PotStateService
    private lateinit var tankStateService: TankStateService
    private lateinit var controller: WateringStateController

    @Before
    fun init() {
        potStateService = mock()
        controller = WateringStateController(potStateService, tankStateService)
    }

    @Test
    fun loadPotWithError() {
        whenever(potStateService.save(any())).thenThrow(RuntimeException())
        val result = controller.sendState("test", 1.0, "test", 1.0, 1.0)

        verify(potStateService, times(1)).save(any())
        verify(tankStateService, never()).save(any())
        assertNotNull(result)
        assertEquals(ResponseStatus.ERROR, result.status)
    }

    @Test
    fun loadTankWithError() {
        whenever(tankStateService.save(any())).thenThrow(RuntimeException())
        val result = controller.sendState("test", 1.0, "test", 1.0, 1.0)

        verify(potStateService, times(1)).save(any())
        verify(tankStateService, times(1)).save(any())
        assertNotNull(result)
        assertEquals(ResponseStatus.ERROR, result.status)
    }

    @Test
    fun success() {
        val result = controller.sendState("test", 1.0, "test", 1.0, 1.0)

        verify(potStateService, times(1)).save(any())
        verify(tankStateService, times(1)).save(any())
        assertNotNull(result)
        assertEquals(ResponseStatus.SUCCESS, result.status)
    }

}