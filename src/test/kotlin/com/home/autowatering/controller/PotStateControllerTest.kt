package com.home.autowatering.controller

import com.home.autowatering.service.interfaces.PotStateService
import com.nhaarman.mockitokotlin2.mock
import org.junit.Before

class PotStateControllerTest {
    private lateinit var service: PotStateService
    private lateinit var controller: PotStateController

    @Before
    fun init() {
        service = mock()
        controller = PotStateController(service)
    }
    //todo
}