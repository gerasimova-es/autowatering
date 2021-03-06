package com.home.autowatering.controller

import com.home.autowatering.service.interfaces.PotService
import com.home.autowatering.service.interfaces.PotStateService
import com.home.autowatering.service.interfaces.WateringSystemService
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.BeforeEach

class PotControllerTest {
    private lateinit var potService: PotService
    private lateinit var potStateService: PotStateService
    private lateinit var wateringSystemService: WateringSystemService
    private lateinit var controller: PotController

    @BeforeEach
    fun init() {
        potService = mock()
        potStateService = mock()
        wateringSystemService = mock()
        controller = PotController(
            potService, potStateService, wateringSystemService
        )
    }

//    @Test
//    fun emptyList() {
//        whenever(potService.findAll()).thenReturn(Future.future<List<Pot>> { arrayListOf<List<Pot>>() })
//
//        val response = controller.list()
//
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.message).isEqualTo("message was handled successfully")
//        assertThat(response.payload).isNotNull
//        assertThat(response.payload).hasSize(0)
//
//        verify(potService, times(1)).findAll()
//        verifyNoMoreInteractions(potService)
//    }
//
//    @Test
//    fun list() {
//        whenever(potService.findAll()).thenReturn(
//            arrayListOf(
//                Pot(
//                    id = 1L,
//                    code = "pot1",
//                    name = "desc1"
//                ),
//                Pot(
//                    id = 2L,
//                    code = "pot2",
//                    name = "desc2"
//                )
//            )
//        )
//
//        val response = controller.list()
//
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.message).isEqualTo("message was handled successfully")
//        assertThat(response.payload).isNotNull
//        assertThat(response.payload).hasSize(2)
//        //todo check elements
//
//        verify(potService, times(1)).findAll()
//        verifyNoMoreInteractions(potService)
//    }

//    @Test
//    fun getNothing() {
//        whenever(potService.find(any())).thenReturn(arrayListOf())
//        val response = controller.info("pot")
//        assertThat(response.status).isEqualTo(StatusType.POT_NOT_FOUND)
//        verify(potService, times(1)).find(any())
//        verifyNoMoreInteractions(potService)
//    }
//
//    @Test
//    fun getException() {
//        whenever(potService.find(any())).thenThrow(IllegalArgumentException())
//        val response = controller.info("pot")
//        assertThat(response.status).isEqualTo(StatusType.INTERNAL_ERROR)
//        verify(potService, times(1)).find(any())
//        verifyNoMoreInteractions(potService)
//    }
//
//    @Test
//    fun get() {
//        val pot = Pot(
//            id = 1L,
//            code = "pot",
//            name = "desc"
//        )
//        whenever(potService.find(any())).thenReturn(arrayListOf(pot))
//
//        val response = controller.info("pot")
//
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.message).isEqualTo("message was handled successfully")
//        assertThat(response.payload).isNotNull
//        assertThat(response.payload).isInstanceOf(PotDto::class.java)
//        assertThat(response.payload?.id).isEqualTo(pot.id)
//        assertThat(response.payload?.code).isEqualTo(pot.code)
//        assertThat(response.payload?.name).isEqualTo(pot.name)
//
//        verify(potService, times(1)).find(any())
//        verifyNoMoreInteractions(potService)
//    }
//
//    @Test
//    fun info() {
//        val pot = Pot(
//            id = 1L,
//            code = "pot",
//            name = "desc"
//        )
//        val potState = PotState(
//            id = 1,
//            pot = pot,
//            date = ZonedDateTime.now(),
//            humidity = 400,
//            watering = true
//        )
//
//        whenever(potService.find(any())).thenReturn(arrayListOf(pot))
//        whenever(potStateService.last(any())).thenReturn(potState)
//
//        val response = controller.info("pot")
//
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.message).isEqualTo("message was handled successfully")
//        assertThat(response.payload).isNotNull
//        assertThat(response.payload).isInstanceOf(PotDto::class.java)
//        assertThat(response.payload?.id).isEqualTo(pot.id)
//        assertThat(response.payload?.code).isEqualTo(pot.code)
//        assertThat(response.payload?.name).isEqualTo(pot.name)
//        assertThat(response.payload?.humidity).isEqualTo(potState.humidity)
//
//        verify(potService, times(1)).find(any())
//        verifyNoMoreInteractions(potService)
//        verify(potStateService, times(1)).last(any())
//        verifyZeroInteractions(potStateService)
//    }
//
//
//    @Test
//    fun savedNewPot() {
//        val pot = PotDto(
//            code = "pot",
//            name = "pot",
//            wateringDuration = 1,
//            checkInterval = 1,
//            minHumidity = 100
//        )
//        whenever(potService.find(any())).thenReturn(arrayListOf())
//        whenever(potService.save(any())).thenReturn(
//            Pot(
//                id = 1,
//                code = "pot",
//                name = "desc",
//                wateringDuration = 1,
//                checkInterval = 1,
//                minHumidity = 100
//            )
//        )
//
//        val response = controller.save(pot)
//
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.message).isEqualTo("message was handled successfully")
//        assertThat(response.payload).isNotNull
//        assertThat(response.payload).isInstanceOf(PotDto::class.java)
//        assertThat(response.payload?.id).isEqualTo(1)
//        assertThat(response.payload?.code).isEqualTo("pot")
//        assertThat(response.payload?.name).isEqualTo("desc")
//
//        verify(potService, times(1)).find(any())
//        verify(potService, times(1)).save(any())
//        verifyNoMoreInteractions(potService)
//    }
//
//    @Test
//    fun savedExistedPot() {
//        whenever(potService.find(any())).thenReturn(
//            arrayListOf(
//                Pot(
//                    id = 1,
//                    code = "pot",
//                    name = "pot1"
//                )
//            )
//        )
//        whenever(potService.merge(any(), any())).thenReturn(
//            Pot(
//                id = 1,
//                code = "pot",
//                name = "pot2"
//            )
//        )
//        whenever(potService.save(any())).thenReturn(
//            Pot(
//                id = 1,
//                code = "pot",
//                name = "pot3"
//            )
//        )
//
//        val response = controller.save(
//            PotDto(
//                code = "pot",
//                name = "pot",
//                minHumidity = 1,
//                checkInterval = 1,
//                wateringDuration = 1
//            )
//        )
//
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.message).isEqualTo("message was handled successfully")
//        assertThat(response.payload).isNotNull
//        assertThat(response.payload).isInstanceOf(PotDto::class.java)
//        assertThat(response.payload?.id).isEqualTo(1)
//        assertThat(response.payload?.code).isEqualTo("pot")
//        assertThat(response.payload?.name).isEqualTo("pot3")
//
//        verify(potService, times(1)).find(any())
//        verify(potService, times(1)).save(any())
//        verify(potService, times(1)).merge(any(), any())
//        verifyNoMoreInteractions(potService)
//    }
//
//    @Test
//    fun findStateWhenWrongDates() {
//        whenever(potService.find(any())).thenReturn(arrayListOf())
//
//        val response = controller.statistic(
//            potCode = "pot",
//            dateFrom = ZonedDateTime.now(),
//            dateTo = ZonedDateTime.now().minusMonths(1)
//        )
//
//        assertThat(response.status).isEqualTo(StatusType.INCORRECT_PERIOD_DATES)
//        verifyZeroInteractions(potService)
//        verifyZeroInteractions(potStateService)
//    }
//
//    @Test
//    fun findStateWhenWrongSlice() {
//        whenever(potService.find(any())).thenReturn(arrayListOf())
//
//        val response = controller.statistic(
//            potCode = "pot",
//            dateFrom = ZonedDateTime.now(),
//            dateTo = ZonedDateTime.now().plusDays(6),
//            slice = SliceType.WEEK
//        )
//
//        assertThat(response.status).isEqualTo(StatusType.INCORRECT_PERIOD_DATES)
//        verifyZeroInteractions(potService)
//        verifyZeroInteractions(potStateService)
//    }
//
//    @Test
//    fun findStateNotExistedPot() {
//        whenever(potService.find(any())).thenReturn(arrayListOf())
//        val response = controller.statistic("pot", ZonedDateTime.now(), ZonedDateTime.now().plusHours(1))
//        assertThat(response.status).isEqualTo(StatusType.POT_NOT_FOUND)
//        verify(potService, times(1)).find(any())
//        verifyNoMoreInteractions(potService)
//        verifyZeroInteractions(potStateService)
//    }
//
//    @Test
//    fun failFindState() {
//        whenever(potService.find(any())).thenReturn(
//            arrayListOf(
//                Pot(
//                    id = 1,
//                    code = "pot"
//                )
//            )
//        )
//        whenever(potStateService.find(any())).thenThrow(IllegalArgumentException("test"))
//        val response = controller.statistic(
//            potCode = "pot",
//            dateFrom = ZonedDateTime.now(),
//            dateTo = ZonedDateTime.now().plusHours(1)
//        )
//        assertThat(response.status).isEqualTo(StatusType.INTERNAL_ERROR)
//        verify(potStateService, times(1)).find(any())
//        verifyNoMoreInteractions(potStateService)
//    }
//
//    @Test
//    fun foundNoStateRecords() {
//        whenever(potService.find(any())).thenReturn(
//            arrayListOf(
//                Pot(
//                    id = 1,
//                    code = "pot"
//                )
//            )
//        )
//        whenever(potStateService.find(any())).thenReturn(arrayListOf())
//
//        val response = controller.statistic("pot", ZonedDateTime.now(), ZonedDateTime.now().plusHours(1))
//
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//
//        verify(potService, times(1)).find(any())
//        verifyNoMoreInteractions(potService)
//
//        verify(potStateService, times(1)).find(any())
//        verifyNoMoreInteractions(potStateService)
//    }
//
//    @Test
//    fun foundOneStateRecord() {
//        val dateFrom = ZonedDateTime.now()
//        val state = PotState(
//            id = 1,
//            pot = Pot(code = "pot"),
//            date = dateFrom,
//            humidity = 1
//        )
//        whenever(potService.find(any())).thenReturn(
//            arrayListOf(
//                Pot(
//                    id = 1,
//                    code = "pot",
//                    name = "pot"
//                )
//            )
//        )
//        whenever(potStateService.find(any())).thenReturn(arrayListOf(state))
//
//        val response = controller.statistic("pot", dateFrom, ZonedDateTime.now().plusHours(1))
//
//        assertThat(response).isNotNull
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.message).isEqualTo("message was handled successfully")
//        assertThat(response.payload).isNotNull
//        assertThat(response.payload).isInstanceOf(ArrayList::class.java)
//        assertThat(response.payload).hasOnlyOneElementSatisfying { element ->
//            assertThat(element.id).isEqualTo(state.id)
//            assertThat(element.date).isEqualTo(state.date)
//            assertThat(element.humidity).isEqualTo(state.humidity)
//        }
//        verify(potService, times(1)).find(any())
//        verifyNoMoreInteractions(potService)
//
//        verify(potStateService, times(1)).find(any())
//        verifyNoMoreInteractions(potStateService)
//    }
//
//    @Test
//    fun foundSomeStateRecords() {
//        val state1 = PotState(
//            id = 1L,
//            pot = Pot(code = "pot"),
//            date = ZonedDateTime.now(),
//            humidity = 1
//        )
//        val state2 = PotState(
//            id = 2L,
//            pot = Pot(code = "pot"),
//            date = ZonedDateTime.now(),
//            humidity = 2
//        )
//        whenever(potService.find(any())).thenReturn(
//            arrayListOf(
//                Pot(
//                    id = 1,
//                    code = "pot",
//                    name = "pot"
//                )
//            )
//        )
//        whenever(potStateService.find(any())).thenReturn(arrayListOf(state1, state2))
//
//        val response = controller.statistic("pot", ZonedDateTime.now(), ZonedDateTime.now().plusHours(1))
//
//        assertThat(response).isNotNull
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.payload).isNotNull
//        assertThat(response.payload).hasSize(2)
//        assertThat(response.payload).hasOnlyElementsOfType(PotStateDto::class.java)
//        //todo check elements
//
//        verify(potService, times(1)).find(any())
//        verifyNoMoreInteractions(potService)
//
//        verify(potStateService, times(1)).find(any())
//        verifyNoMoreInteractions(potStateService)
//    }
//
//    @Test
//    fun saveStateNotExistedPot() {
//        whenever(potStateService.save(any())).thenThrow(PotNotFoundException(1))
//
//        val response = controller.saveState(
//            PotStateDto(
//                potCode = "pot",
//                date = ZonedDateTime.now(),
//                humidity = 10,
//                watering = false
//            )
//        )
//        assertThat(response.status).isEqualTo(StatusType.POT_NOT_FOUND)
//        assertThat(response.message).isNotNull()
//        assertThat(response.payload).isNull()
//
//        verify(potStateService, times(1)).save(any())
//        verifyNoMoreInteractions(potStateService)
//    }
//
//
//    @Test
//    fun saveState() {
//        val state = PotState(
//            id = 1L,
//            pot = Pot(code = "pot"),
//            date = ZonedDateTime.now(),
//            humidity = 1,
//            watering = false
//        )
//        whenever(potStateService.save(any())).thenReturn(state)
//
//        val response = controller.saveState(
//            PotStateDto(
//                potCode = "pot",
//                date = ZonedDateTime.now(),
//                humidity = 10,
//                watering = false
//            )
//        )
//
//        assertThat(response).isNotNull
//        assertThat(response.status).isEqualTo(StatusType.SUCCESS)
//        assertThat(response.message).isEqualTo("message was handled successfully")
//        assertThat(response.payload).isNull()
//
//        verify(potStateService, times(1)).save(any())
//        verifyNoMoreInteractions(potStateService)
//
//    }

}