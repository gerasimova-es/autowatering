package com.home.autowatering.controller

import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.service.interfaces.PotStateService
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class PotStateControllerTest {
    private lateinit var service: PotStateService
    private lateinit var controller: PotStateController

    @Before
    fun init() {
        service = mock()
        controller = PotStateController(service)
    }

    @Test
    fun saveWithError() {
        whenever(service.save(any())).thenThrow(SavingException(RuntimeException()))
        try {
            val response = controller.save(PotStateDto(potName = "", date = Date(), humidity = 0.0))
            fail("expected SavingException")
        } catch (ignored: SavingException) {
            verify(service, times(1)).save(any())
            verifyNoMoreInteractions(service)
        }
    }

    @Test
    fun saveSuccessfully() {
        val dto = PotStateDto(potName = "pot", date = Date(), humidity = 1.0)
        whenever(service.save(any())).thenReturn(
            PotState(1L, Pot(name = "pot"), dto.date!!, 1.0)
        )

        val response = controller.save(dto)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isEqualTo(dto)

        verify(service, times(1)).save(any())
        verifyNoMoreInteractions(service)
    }

    @Test
    fun findWithError() {
        whenever(service.find(any())).thenThrow(IllegalArgumentException("test"))
        try {
            controller.find("pot", Date(), Date())
            fail("expected IllegalArgumentException")
        } catch (ignored: IllegalArgumentException) {
            verify(service, times(1)).find(any())
            verifyNoMoreInteractions(service)
        }
    }

    @Test
    fun foundNothing() {
        whenever(service.find(any())).thenReturn(arrayListOf())

        val response = controller.find("pot", Date(), Date())

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isEqualTo(arrayListOf<PotStateDto>())

        verify(service, times(1)).find(any())
        verifyNoMoreInteractions(service)
    }

    @Test
    fun foundOne() {
        val state = PotState(1L, Pot(name = "pot"), Date(), 1.0)
        whenever(service.find(any())).thenReturn(arrayListOf(state))

        val response = controller.find("pot", Date(), Date())

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(ArrayList::class.java)
        assertThat(response.payload).hasOnlyOneElementSatisfying { element ->
            assertThat(element.date).isEqualTo(state.date)
            assertThat(element.humidity).isEqualTo(state.humidity)
            assertThat(element.potName).isEqualTo(state.pot.name)
        }

        verify(service, times(1)).find(any())
        verifyNoMoreInteractions(service)
    }

    @Test
    fun foundSome() {
        val state1 = PotState(1L, Pot(name = "pot1"), Date(), 1.0)
        val state2 = PotState(2L, Pot(name = "pot2"), Date(), 2.0)
        whenever(service.find(any())).thenReturn(arrayListOf(state1, state2))

        val response = controller.find("pot", Date(), Date())

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.payload).isNotNull
        assertThat(response.payload).hasSize(2)
        assertThat(response.payload).hasOnlyElementsOfType(PotStateDto::class.java)
        assertThat(response.payload).containsExactlyInAnyOrder()
//todo check both
//        assertThat(response.payload).satisfies() { element ->
//            assertThat(element.id).isEqualTo(state.id)
//            assertThat(element.date).isEqualTo(state.date)
//            assertThat(element.humidity).isEqualTo(state.humidity)
//            assertThat(element.potName).isEqualTo(state.pot.name)
//        }

        verify(service, times(1)).find(any())
        verifyNoMoreInteractions(service)
    }
}