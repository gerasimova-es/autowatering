package com.home.autowatering.service.impl

import com.home.autowatering.dao.interfaces.PotDao
import com.home.autowatering.dao.interfaces.PotStateDao
import com.home.autowatering.dao.interfaces.TankStateDao
import com.home.autowatering.exception.SavingException
import com.home.autowatering.model.Pot
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert
import org.junit.Before

class WateringStateServiceImplTest {
    private lateinit var potDao: PotDao
    private lateinit var potStateDao: PotStateDao
    private lateinit var tankStateDao: TankStateDao
    //private lateinit var service: WateringStateService

    @Before
    fun init() {
        potDao = mock()
        potStateDao = mock()
        tankStateDao = mock()
        // service = WateringStateServiceImpl(potDao, potStateDao, tankStateDao)
    }

    //@Test
    fun loadWithEmptyPotName() {
        try {
            //   service.load(WateringState("", 0.0, "", 0.0))
            Assert.fail("expected IllegalArgumentException")
        } catch (exc: IllegalArgumentException) {
            verify(potDao, never()).save(any())
            verify(potStateDao, never()).save(any())
            verify(tankStateDao, never()).save(any())
        }
    }

    //@Test
    fun loadWithZeroPotHumidity() {
        try {
            // service.load(WateringState("test", 0.0, "", 0.0))
            Assert.fail("expected IllegalArgumentException")
        } catch (exc: IllegalArgumentException) {
            verify(potDao, never()).save(any())
            verify(potStateDao, never()).save(any())
            verify(tankStateDao, never()).save(any())
        }
    }

    //@Test
    fun loadWithNegativePotHumidity() {
        try {
            // service.load(WateringState("test", -7.0, "", 0.0))
            Assert.fail("expected IllegalArgumentException")
        } catch (exc: IllegalArgumentException) {
            verify(potDao, never()).save(any())
            verify(potStateDao, never()).save(any())
            verify(tankStateDao, never()).save(any())
        }
    }

    //@Test
    fun loadWithEmptyTankName() {
        try {
            // service.load(WateringState("test", 7.0, "", 0.0))
            Assert.fail("expected IllegalArgumentException")
        } catch (exc: IllegalArgumentException) {
            verify(potDao, never()).save(any())
            verify(potStateDao, never()).save(any())
            verify(tankStateDao, never()).save(any())
        }
    }

    //@Test
    fun loadWithNegativeTankVolume() {
        try {
//            service.load(WateringState("test", 7.0, "test", -8.0))
            Assert.fail("expected IllegalArgumentException")
        } catch (exc: IllegalArgumentException) {
            verify(potDao, never()).save(any())
            verify(potStateDao, never()).save(any())
            verify(tankStateDao, never()).save(any())
        }
    }

    //@Test
    fun loadWithPotFindingError() {
        whenever(potDao.findByName(any())).thenThrow(SavingException(RuntimeException()))
        try {
//            service.load(WateringState("test", 7.0, "test", 8.0))
            Assert.fail("expected SavingException")
        } catch (exc: SavingException) {
            verify(potDao, times(1)).findByName(any())
            verify(potDao, never()).save(any())
            verify(potStateDao, never()).save(any())
            verify(tankStateDao, never()).save(any())
        }
    }

    //@Test
    fun loadWithSavingPotError() {
        whenever(potDao.findByName(any())).thenReturn(null)
        whenever(potDao.save(any())).thenThrow(SavingException(RuntimeException()))
        try {
//            service.load(WateringState("test", 7.0, "test", 8.0))
            Assert.fail("expected SavingException")
        } catch (exc: SavingException) {
            verify(potDao, times(1)).findByName(any())
            verify(potDao, times(1)).save(any())
            verify(potStateDao, never()).save(any())
            verify(tankStateDao, never()).save(any())
        }
    }

    //@Test
    fun loadWithSavingPotStateError() {
        val pot = Pot(1L, "name")
        whenever(potDao.findByName(any())).thenReturn(pot)
        //whenever(potStateDao.save(eq(pot))).thenThrow(SavingException(RuntimeException()))
        try {
            //  service.load(WateringState("test", 7.0, "test", 8.0))
            Assert.fail("expected SavingException")
        } catch (exc: SavingException) {
            verify(potDao, times(1)).findByName(any())
            verify(potDao, never()).save(any())
            verify(potStateDao, times(1)).save(any())
            verify(tankStateDao, never()).save(any())
        }
    }

    //@Test
    fun loadWithSavingTankStateError() {
        whenever(potDao.findByName(any())).thenReturn(Pot(1L, "name"))
        whenever(tankStateDao.save(any())).thenThrow(SavingException(RuntimeException()))
        try {
            //  service.load(WateringState("test", 7.0, "test", 8.0))
            Assert.fail("expected SavingException")
        } catch (exc: SavingException) {
            verify(potDao, times(1)).findByName(any())
            verify(potDao, never()).save(any())
            verify(potStateDao, times(1)).save(any())
            verify(tankStateDao, times(1)).save(any())
        }
    }

    //@Test
    fun successLoad() {
        whenever(potDao.findByName(any())).thenReturn(Pot(1L, "name"))

        //  service.load(WateringState("test", 7.0, "test", 8.0))

        verify(potDao, times(1)).findByName(any())
        verify(potDao, never()).save(any())
        verify(potStateDao, times(1)).save(any())
        verify(tankStateDao, times(1)).save(any())

    }

}