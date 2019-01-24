package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.model.Pot
import com.home.autowatering.service.interfaces.PotService
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class PotServiceImplTest {
    private lateinit var potDao: PotDao
    private lateinit var service: PotService

    @Before
    fun init() {
        potDao = mock()
        service = PotServiceImpl(potDao)
    }

    @Test
    fun getAll() {
        whenever(potDao.getAll()).thenReturn(arrayListOf(Pot(name = "pot1"), Pot(name = "2")))

        val result = service.getAll()

        assertThat(result).isNotNull
        assertThat(result).hasSize(2)
        //todo check pots

        verify(potDao, times(1)).getAll()
        verifyNoMoreInteractions(potDao)
    }

    @Test
    fun getById() {
        val pot = Pot(name = "pot1")
        whenever(potDao.getById(any())).thenReturn(pot)

        val result = service.getById(1)

        assertThat(result).isNotNull
        assertThat(result).isEqualTo(pot)

        verify(potDao, times(1)).getById(eq(1))
        verifyNoMoreInteractions(potDao)
    }

    @Test
    fun getByName() {
        val pot = Pot(name = "pot1")
        whenever(potDao.getByName(any())).thenReturn(pot)

        val result = service.getByName("pot1")

        assertThat(result).isNotNull
        assertThat(result).isEqualTo(pot)

        verify(potDao, times(1)).getByName(eq("pot1"))
        verifyNoMoreInteractions(potDao)
    }
}