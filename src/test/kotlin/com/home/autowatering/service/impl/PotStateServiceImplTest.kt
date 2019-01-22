package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.Pot
import com.home.autowatering.model.PotState
import com.home.autowatering.model.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*

class PotStateServiceImplTest {
    private lateinit var potDao: PotDao
    private lateinit var potStateDao: PotStateDao
    private lateinit var service: PotStateService

    @Before
    fun init() {
        potDao = mock()
        potStateDao = mock()
        service = PotStateServiceImpl(potDao, potStateDao)
    }

    @Test
    fun findError() {
        whenever(potStateDao.find(any())).thenThrow(IllegalArgumentException("test"))
        try {
            service.find(PotStateFilter.build())
            fail("expected IllegalArgumentException")
        } catch (ignored: IllegalArgumentException) {
            verify(potStateDao, times(1)).find(any())
            verifyNoMoreInteractions(potStateDao)
        }
    }

    @Test
    fun findSuccess() {
        val states = arrayListOf(
            PotState(
                pot = Pot(name = "pot"),
                date = Date(),
                humidity = 1.0
            )
        )
        whenever(potStateDao.find(any())).thenReturn(states)

        val result = service.find(PotStateFilter.build())
        assertThat(result).isSameAs(states)

        verify(potStateDao, times(1)).find(any())
        verifyNoMoreInteractions(potStateDao)
    }

    @Test
    fun potSearchException() {
        whenever(potDao.findByName(any())).thenThrow(SavingException(RuntimeException()))
        try {
            service.save(
                PotState(
                    pot = Pot(name = "pot"),
                    date = Date(),
                    humidity = 1.0
                )
            )
            fail("expected SavingException")
        } catch (ignored: SavingException) {
            verify(potDao, times(1)).findByName(any())
            verifyNoMoreInteractions(potDao)
            verifyNoMoreInteractions(potStateDao)
        }
    }

    @Test
    fun potSaveException() {
        whenever(potDao.findByName(any())).thenReturn(null)
        whenever(potDao.save(any())).thenThrow(SavingException(RuntimeException()))
        try {
            service.save(
                PotState(
                    pot = Pot(name = "pot"),
                    date = Date(),
                    humidity = 1.0
                )
            )
            fail("expected SavingException")
        } catch (ignored: SavingException) {
            verify(potDao, times(1)).findByName(any())
            verify(potDao, times(1)).save(any())
            verifyNoMoreInteractions(potDao)
            verifyNoMoreInteractions(potStateDao)
        }
    }

    @Test
    fun potFound() {
        whenever(potDao.findByName(any())).thenReturn(Pot(name = "pot"))
        val state = PotState(
            pot = Pot(name = "pot"),
            date = Date(),
            humidity = 1.0
        )
        service.save(state)
        verify(potDao, times(1)).findByName(any())
        verifyNoMoreInteractions(potDao)
        verify(potStateDao, times(1)).save(eq(state))
        verifyNoMoreInteractions(potStateDao)
    }


    @Test
    fun stateSavingError() {
        whenever(potDao.findByName(any())).thenReturn(Pot(name = "pot"))
        whenever(potStateDao.save(any())).thenThrow(SavingException(RuntimeException()))
        try {
            service.save(
                PotState(
                    pot = Pot(name = "pot"),
                    date = Date(),
                    humidity = 1.0
                )
            )
            fail("expected SavingException")
        } catch (ignored: SavingException) {
            verify(potDao, times(1)).findByName(any())
            verifyNoMoreInteractions(potDao)
            verify(potStateDao, times(1)).save(any())
            verifyNoMoreInteractions(potStateDao)
        }
    }

}