package com.home.autowatering.controller

import com.home.autowatering.dto.TankStateDto
import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.model.TankState
import com.home.autowatering.service.interfaces.TankStateService
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*

class TankStateControllerTest {
    private lateinit var service: TankStateService
    private lateinit var controller: TankStateController

    @Before
    fun init() {
        service = mock()
        controller = TankStateController(service)
    }

    @Test
    fun saveError() {
        whenever(service.save(any())).thenThrow(IllegalArgumentException("test"))
        try {
            val response = controller.save(TankStateDto(name = "", date = Date().time, volume = 0.0, filled = 0.0))
            fail("expected IllegalArgumentException")
        } catch (ignored: IllegalArgumentException) {
            verify(service, times(1)).save(any())
        }
    }

    @Test
    fun saveSuccess() {
        val state = TankState(name = "", date = Date(), volume = 1.0, filled = 0.0)
        whenever(service.save(any())).thenReturn(state)

        val response = controller.save(TankStateDto(name = "name", date = Date().time, volume = 0.0, filled = 0.0))
        assertThat(response).isNotNull

        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.payload!!.name).isEqualTo(state.name)
        assertThat(response.payload!!.date).isEqualTo(state.date)
        assertThat(response.payload!!.volume).isEqualTo(state.volume)
        assertThat(response.payload!!.filled).isEqualTo(state.filled)
        assertThat(response.message).isEqualTo("message was handled successfully")
    }

}