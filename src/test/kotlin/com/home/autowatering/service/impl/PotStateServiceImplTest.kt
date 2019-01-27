package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotStateDao
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
    private lateinit var potStateDao: PotStateDao
    private lateinit var service: PotStateService

    @Before
    fun init() {
        potStateDao = mock()
        service = PotStateServiceImpl(potStateDao)
    }

    @Test
    fun findError() {
        whenever(potStateDao.find(any())).thenThrow(IllegalArgumentException("test"))
        try {
            service.find(PotStateFilter(Pot(code = "pot")))
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
                id = 1,
                pot = Pot(code = "pot"),
                date = Date(),
                humidity = 1.0
            )
        )
        whenever(potStateDao.find(any())).thenReturn(states)

        val result = service.find(PotStateFilter(Pot(code = "pot")))
        assertThat(result).isSameAs(states)

        verify(potStateDao, times(1)).find(any())
        verifyNoMoreInteractions(potStateDao)
    }

}