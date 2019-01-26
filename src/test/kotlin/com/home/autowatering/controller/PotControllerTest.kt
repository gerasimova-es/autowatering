package com.home.autowatering.controller

import com.home.autowatering.dto.PotDto
import com.home.autowatering.dto.PotStateDto
import com.home.autowatering.dto.response.ResponseStatus
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.service.interfaces.PotService
import com.home.autowatering.service.interfaces.PotStateService
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
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
            name = "pot",
            description = "desc",
            state = PotState(
                1, Date(), 1.0
            )
        )
        whenever(potService.find(any())).thenReturn(pot)

        val response = controller.get(1)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(pot.id)
        assertThat(response.payload?.name).isEqualTo(pot.name)
        assertThat(response.payload?.description).isEqualTo(pot.description)
        assertThat(response.payload?.state?.id).isEqualTo(pot.state?.id)
        assertThat(response.payload?.state?.date).isEqualTo(pot.state?.date?.time)
        assertThat(response.payload?.state?.humidity).isEqualTo(pot.state?.humidity)

        verify(potService, times(1)).find(any())
        verifyNoMoreInteractions(potService)
    }

    @Test
    fun gotWithoutState() {
        val pot = Pot(
            id = 1L,
            name = "pot",
            description = "desc"
        )
        whenever(potService.find(any())).thenReturn(pot)

        val response = controller.get(1)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(pot.id)
        assertThat(response.payload?.name).isEqualTo(pot.name)
        assertThat(response.payload?.description).isEqualTo(pot.description)
        assertThat(response.payload?.state).isNull()

        verify(potService, times(1)).find(any())
        verifyNoMoreInteractions(potService)
    }

    @Test
    fun savedWithStateNewPot() {
        val date = Date()
        val pot = PotDto(
            name = "pot",
            description = "pot",
            state = PotStateDto(
                date = date.time,
                humidity = 1.0
            )
        )
        whenever(potService.find(any())).thenReturn(null)
        whenever(potService.save(any())).thenReturn(
            Pot(
                id = 1,
                name = "pot",
                description = "desc",
                state = PotState(
                    1,
                    date,
                    1.0
                )
            )
        )

        val response = controller.save(pot)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(1)
        assertThat(response.payload?.name).isEqualTo("pot")
        assertThat(response.payload?.description).isEqualTo("desc")
        assertThat(response.payload?.state?.id).isEqualTo(1)
        assertThat(response.payload?.state?.date).isEqualTo(date.time)
        assertThat(response.payload?.state?.humidity).isEqualTo(1.0)

        verify(potService, times(1)).find(any())
        verify(potService, times(1)).save(any())
        verifyNoMoreInteractions(potService)
    }

    @Test
    fun savedWithStateExistedPot() {
        val date = Date()
        whenever(potService.find(any())).thenReturn(
            Pot(
                id = 1,
                name = "pot",
                description = "pot1"
            )
        )
        whenever(potService.merge(any(), any())).thenReturn(
            Pot(
                id = 1,
                name = "pot",
                description = "pot2"
            )
        )
        whenever(potService.save(any())).thenReturn(
            Pot(
                id = 1,
                name = "pot",
                description = "pot3",
                state = PotState(
                    1,
                    date,
                    1.0
                )
            )
        )

        val response = controller.save(
            PotDto(
                name = "pot",
                description = "pot",
                state = PotStateDto(
                    date = date.time,
                    humidity = 1.0
                )
            )
        )

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(1)
        assertThat(response.payload?.name).isEqualTo("pot")
        assertThat(response.payload?.description).isEqualTo("pot3")
        assertThat(response.payload?.state?.id).isEqualTo(1)
        assertThat(response.payload?.state?.date).isEqualTo(date.time)
        assertThat(response.payload?.state?.humidity).isEqualTo(1.0)

        verify(potService, times(1)).find(any())
        verify(potService, times(1)).merge(any(), any())
        verify(potService, times(1)).save(any())
        verifyNoMoreInteractions(potService)
    }

    @Test
    fun savedWithoutStateNewPot() {
        val pot = PotDto(
            name = "pot",
            description = "pot"
        )
        whenever(potService.find(any())).thenReturn(null)
        whenever(potService.save(any())).thenReturn(
            Pot(
                id = 1,
                name = "pot",
                description = "desc"
            )
        )

        val response = controller.save(pot)

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(1)
        assertThat(response.payload?.name).isEqualTo("pot")
        assertThat(response.payload?.description).isEqualTo("desc")
        assertThat(response.payload?.state).isNull()

        verify(potService, times(1)).find(any())
        verify(potService, times(1)).save(any())
        verifyNoMoreInteractions(potService)
    }

    @Test
    fun savedWithoutStateExistedPot() {
        whenever(potService.find(any())).thenReturn(
            Pot(
                id = 1,
                name = "pot",
                description = "pot1"
            )
        )
        whenever(potService.merge(any(), any())).thenReturn(
            Pot(
                id = 1,
                name = "pot",
                description = "pot2"
            )
        )
        whenever(potService.save(any())).thenReturn(
            Pot(
                id = 1,
                name = "pot",
                description = "pot3"
            )
        )

        val response = controller.save(
            PotDto(
                name = "pot",
                description = "pot"
            )
        )

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isNotNull
        assertThat(response.payload).isInstanceOf(PotDto::class.java)
        assertThat(response.payload?.id).isEqualTo(1)
        assertThat(response.payload?.name).isEqualTo("pot")
        assertThat(response.payload?.description).isEqualTo("pot3")
        assertThat(response.payload?.state).isNull()

        verify(potService, times(1)).find(any())
        verify(potService, times(1)).save(any())
        verify(potService, times(1)).merge(any(), any())
        verifyNoMoreInteractions(potService)
    }

    @Test
    fun failFindState() {
        whenever(potStateService.find(any())).thenThrow(IllegalArgumentException("test"))
        try {
            controller.states("pot", Date(), Date())
            Assert.fail("expected IllegalArgumentException")
        } catch (ignored: IllegalArgumentException) {
            verify(potStateService, times(1)).find(any())
            verifyNoMoreInteractions(potStateService)
        }
    }

    @Test
    fun foundNoStateRecords() {
        whenever(potStateService.find(any())).thenReturn(arrayListOf())

        val response = controller.states("pot", Date(), Date())

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.message).isEqualTo("message was handled successfully")
        assertThat(response.payload).isEqualTo(arrayListOf<PotStateDto>())

        verify(potStateService, times(1)).find(any())
        verifyNoMoreInteractions(potStateService)
    }

    @Test
    fun foundOneStateRecord() {
        val state = PotState(1, Date(), 1.0)
        whenever(potStateService.find(any())).thenReturn(arrayListOf(state))

        val response = controller.states("pot", Date(), Date())

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

        verify(potStateService, times(1)).find(any())
        verifyNoMoreInteractions(potStateService)
    }

    @Test
    fun foundSomeStateRecords() {
        val state1 = PotState(1L, Date(), 1.0)
        val state2 = PotState(2L, Date(), 2.0)
        whenever(potStateService.find(any())).thenReturn(arrayListOf(state1, state2))

        val response = controller.states("pot", Date(), Date())

        assertThat(response).isNotNull
        assertThat(response.status).isEqualTo(ResponseStatus.SUCCESS)
        assertThat(response.payload).isNotNull
        assertThat(response.payload).hasSize(2)
        assertThat(response.payload).hasOnlyElementsOfType(PotStateDto::class.java)
        //todo check both

        verify(potStateService, times(1)).find(any())
        verifyNoMoreInteractions(potStateService)
    }

}