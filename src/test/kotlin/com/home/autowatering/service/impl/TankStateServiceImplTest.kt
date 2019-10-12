package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.business.TankState
import com.home.autowatering.service.interfaces.TankStateService
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class TankStateServiceImplTest {
    private lateinit var tankStateDao: TankStateDao
    private lateinit var service: TankStateService

    @BeforeEach
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
            fail<String>("expected SavingException")
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