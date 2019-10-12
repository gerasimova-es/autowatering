package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.PotState
import com.home.autowatering.model.business.filter.PotStateFilter
import com.home.autowatering.service.interfaces.PotStateService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class PotStateServiceImplTest {
    private lateinit var potStateDao: PotStateDao
    private lateinit var service: PotStateService

    @BeforeEach
    fun init() {
        potStateDao = mock()
        service = PotStateServiceImpl(potStateDao)
    }

    @Test
    fun findError() {
        whenever(potStateDao.find(any())).thenThrow(IllegalArgumentException("test"))
        try {
            service.find(PotStateFilter(Pot(code = "pot")))
            Assertions.fail<String>("expected IllegalArgumentException")
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
                humidity = 1
            )
        )
        whenever(potStateDao.find(any())).thenReturn(states)

        val result = service.find(PotStateFilter(Pot(code = "pot")))
        assertThat(result).isSameAs(states)

        verify(potStateDao, times(1)).find(any())
        verifyNoMoreInteractions(potStateDao)
    }

}