package com.home.autowatering.controller

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.service.interfaces.PotStateService
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*

class PotStateControllerTest {
    private lateinit var service: PotStateService
    private lateinit var controller: PotStateController

    @Before
    fun init() {
        service = mock()
        controller = PotStateController(service)
    }

    @Test
    fun saveError() {
        whenever(service.save(any())).thenThrow(IllegalArgumentException("test"))
        try {
            val response = controller.save(PotStateDto("", Date(), 0.0))
            fail("expected IllegalArgumentException")
        } catch (ignored: IllegalArgumentException) {
            verify(service, times(1)).save(any())
        }
    }

    @Test
    fun saveSuccess() {
        val state = PotState(0, Pot("pot"), Date(), 1.0)
        whenever(service.save(any())).thenReturn(state)

        val response = controller.save(PotStateDto("pot", Date(), 1.0))
        assertThat(response).isNotNull

        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.payload!!.id).isEqualTo(state.id)
        assertThat(response.payload!!.potName).isEqualTo(state.pot.name)
        assertThat(response.payload!!.date).isEqualTo(state.date)
        assertThat(response.payload!!.humidity).isEqualTo(state.humidity)
        assertThat(response.message).isEqualTo("message was handled successfully")
    }
}