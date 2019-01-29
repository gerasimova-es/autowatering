package com.home.autowatering.controller

import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.exception.PotNotFoundException
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.service.interfaces.PotService
import com.home.autowatering.service.interfaces.PotStateService
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*

class PotControllerTest {
    private lateinit var potService: PotService
    private lateinit var potStateService: PotStateService
    private lateinit var controller: PotController

    @Before
    fun init() {
        potService = mock()
        potStateService = mock()
        controller = PotController(potService, potStateService)
    }

    @Test
    fun got() {
        val pot = Pot(
            id = 1L,
            code = "pot",
            name = "desc"
        )
        whenever(potService.find(any())).thenReturn(arrayListOf(pot))

        val response = controller.get(1)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(pot.id)
        assertThat(response.payload?.code).isEqualTo(pot.code)
        assertThat(response.payload?.name).isEqualTo(pot.name)

        verify(potService, times(1)).find(any())
        verifyNoMoreInteractions(potService)
    }


    @Test
    fun savedNewPot() {
        val pot = PotDto(
            code = "pot",
            name = "pot"
        )
        whenever(potService.find(any())).thenReturn(arrayListOf())
        whenever(potService.save(any())).thenReturn(
            Pot(
                id = 1,
                code = "pot",
                name = "desc"
            )
        )

        val response = controller.save(pot)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(1)
        assertThat(response.payload?.code).isEqualTo("pot")
        assertThat(response.payload?.name).isEqualTo("desc")

        verify(potService, times(1)).find(any())
        verify(potService, times(1)).save(any())
        verifyNoMoreInteractions(potService)
    }

    @Test
    fun savedExistedPot() {
        whenever(potService.find(any())).thenReturn(
            arrayListOf(
                Pot(
                    id = 1,
                    code = "pot",
                    name = "pot1"
                )
            )
        )
        whenever(potService.merge(any(), any())).thenReturn(
            Pot(
                id = 1,
                code = "pot",
                name = "pot2"
            )
        )
        whenever(potService.save(any())).thenReturn(
            Pot(
                id = 1,
                code = "pot",
                name = "pot3"
            )
        )

        val response = controller.save(
            PotDto(
                code = "pot",
                name = "pot"
            )
        )

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(1)
        assertThat(response.payload?.code).isEqualTo("pot")
        assertThat(response.payload?.name).isEqualTo("pot3")

        verify(potService, times(1)).find(any())
        verify(potService, times(1)).save(any())
        verify(potService, times(1)).merge(any(), any())
        verifyNoMoreInteractions(potService)
    }

    @Test
    fun findStateWithNotExistedPot() {
        whenever(potService.find(any())).thenReturn(arrayListOf())
        try {
            controller.states(1, Date(), Date())
            fail("expected PotNotFoundException")
        } catch (ignored: PotNotFoundException) {
            verify(potService, times(1)).find(any())
            verifyNoMoreInteractions(potService)
            verifyZeroInteractions(potStateService)
        }
    }

    @Test
    fun failFindState() {
        whenever(potService.find(any())).thenReturn(arrayListOf(Pot(id = 1, code = "pot")))
        whenever(potStateService.find(any())).thenThrow(IllegalArgumentException("test"))
        try {
            controller.states(1, Date(), Date())
            fail("expected IllegalArgumentException")
        } catch (ignored: IllegalArgumentException) {
            verify(potStateService, times(1)).find(any())
            verifyNoMoreInteractions(potStateService)
        }
    }

    @Test
    fun foundNoStateRecords() {
        whenever(potService.find(any())).thenReturn(arrayListOf(Pot(id = 1, code = "pot")))
        whenever(potStateService.find(any())).thenReturn(arrayListOf())

        val response = controller.states(1, Date(), Date())

        verify(potService, times(1)).find(any())
        verifyNoMoreInteractions(potService)

        verify(potStateService, times(1)).find(any())
        verifyNoMoreInteractions(potStateService)
    }

    @Test
    fun foundOneStateRecord() {
        val state = PotState(1, Pot(code = "pot"), Date(), 1.0)
        whenever(potService.find(any())).thenReturn(arrayListOf(Pot(id = 1, code = "pot", name = "pot")))
        whenever(potStateService.find(any())).thenReturn(arrayListOf(state))

        val response = controller.states(1, Date(), Date())

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(ArrayList::class.java)
        assertThat(response.payload).hasOnlyOneElementSatisfying { element ->
            assertThat(element.id).isEqualTo(state.id)
            assertThat(element.date).isEqualTo(state.date.time)
            assertThat(element.humidity).isEqualTo(state.humidity)
        }
        verify(potService, times(1)).find(any())
        verifyNoMoreInteractions(potService)

        verify(potStateService, times(1)).find(any())
        verifyNoMoreInteractions(potStateService)
    }

    @Test
    fun foundSomeStateRecords() {
        val state1 = PotState(1L, Pot(code = "pot"), Date(), 1.0)
        val state2 = PotState(2L, Pot(code = "pot"), Date(), 2.0)
        whenever(potService.find(any())).thenReturn(arrayListOf(Pot(id = 1, code = "pot", name = "pot")))
        whenever(potStateService.find(any())).thenReturn(arrayListOf(state1, state2))

        val response = controller.states(1, Date(), Date())

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.payload).isNotNull
        assertThat(response.payload).hasSize(2)
        assertThat(response.payload).hasOnlyElementsOfType(PotStateDto::class.java)
        //todo check both
        verify(potService, times(1)).find(any())
        verifyNoMoreInteractions(potService)

        verify(potStateService, times(1)).find(any())
        verifyNoMoreInteractions(potStateService)
    }

}