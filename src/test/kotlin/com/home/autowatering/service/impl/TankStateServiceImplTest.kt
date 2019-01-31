package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.business.TankState
import com.home.autowatering.service.interfaces.TankStateService
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.util.*

class TankStateServiceImplTest {
    private lateinit var tankStateDao: TankStateDao
    private lateinit var service: TankStateService

    @Before
    fun init() {
        tankStateDao = mock()
        service = TankStateServiceImpl(tankStateDao)
    }

    @Test
    fun error() {
        val state = TankState(
            name = "test",
            date = Date(),
            volume = 1.0,
            filled = 1.0
        )
        whenever(tankStateDao.save(eq(state))).thenThrow(SavingException(RuntimeException()))
        try {
            service.save(state)
            fail("expected SavingException")
        } catch (ignored: SavingException) {
            verify(tankStateDao, times(1)).save(eq(state))
            verifyNoMoreInteractions(tankStateDao)
        }
    }


    @Test
    fun success() {
        val state = TankState(
            name = "test",
            date = Date(),
            volume = 1.0,
            filled = 1.0
        )
        whenever(tankStateDao.save(eq(state))).thenReturn(state)

        service.save(state)

        verify(tankStateDao, times(1)).save(eq(state))
        verifyNoMoreInteractions(tankStateDao)
    }
}