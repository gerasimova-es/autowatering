package com.home.autowatering.controller

import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.model.Pot
import com.home.autowatering.service.interfaces.PotService
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class PotControllerTest {
    private lateinit var service: PotService
    private lateinit var controller: PotController

    @Before
    fun init() {
        service = mock()
        controller = PotController(service)
    }

    @Test
    fun found() {
        val pot = Pot(id = 1L, name = "pot", description = "desc")
        whenever(service.getById(any())).thenReturn(pot)

        val response = controller.get(1)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload!!.id).isEqualTo(pot.id)
        assertThat(response.payload!!.name).isEqualTo(pot.name)
        assertThat(response.payload!!.description).isEqualTo(pot.description)

        verify(service, times(1)).getById(eq(1))
        verifyNoMoreInteractions(service)
    }

}