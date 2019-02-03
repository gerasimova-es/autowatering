package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.dao.interfaces.WateringSystemDao
import com.home.autowatering.model.business.Pot
import com.home.autowatering.model.business.filter.PotFilter
import com.home.autowatering.service.interfaces.PotService
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class PotServiceImplTest {
    private lateinit var potDao: PotDao
    private lateinit var service: PotService
    private lateinit var wateringSystemDao: WateringSystemDao

    @Before
    fun init() {
        potDao = mock()
        wateringSystemDao = mock()
        service = PotServiceImpl(potDao, wateringSystemDao)
    }

    @Test
    fun findAll() {
        whenever(potDao.findAll()).thenReturn(
            arrayListOf(
                Pot(code = "pot1"),
                Pot(code = "2")
            )
        )

        val result = service.findAll()

        assertThat(result).isNotNull
        assertThat(result).hasSize(2)
        //todo check pots

        verify(potDao, times(1)).findAll()
        verifyNoMoreInteractions(potDao)
    }

    @Test
    fun findById() {
        val pot = Pot(code = "pot1")
        whenever(potDao.findById(any())).thenReturn(pot)

        val result = service.find(PotFilter(id = 1, code = "code"))

        assertThat(result).isNotNull
        assertThat(result).isEqualTo(arrayListOf(pot))

        verify(potDao, times(1)).findById(eq(1))
        verifyNoMoreInteractions(potDao)
    }

    @Test
    fun findByName() {
        val pot = Pot(code = "pot1")
        whenever(potDao.findByCode(any())).thenReturn(pot)

        val result = service.find(PotFilter(code = "code"))

        assertThat(result).isNotNull
        assertThat(result).isEqualTo(arrayListOf(pot))

        verify(potDao, times(1)).findByCode(eq("code"))
        verifyNoMoreInteractions(potDao)
    }

    @Test
    fun merge() {
        val source = Pot(
            id = 1,
            code = "pot1",
            name = "desc1"
        )
        val target = Pot(
            id = 2,
            code = "pot2",
            name = "desc2"
        )

        val result = service.merge(source, target)

        assertThat(result).isNotNull
        assertThat(result.code).isEqualTo(target.code)
        assertThat(result.name).isEqualTo(source.name)
    }


    @Test
    fun save() {
        whenever(potDao.save(any()))
            .thenReturn(Pot(id = 1, code = "pot1", name = "pot1"))

        val result = service.save(Pot(code = "pot1", name = "pot1"))

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(1)
        assertThat(result.code).isEqualTo("pot1")
        assertThat(result.name).isEqualTo("pot1")

        verify(potDao, times(1)).save(any())
        verifyNoMoreInteractions(potDao)
    }
}