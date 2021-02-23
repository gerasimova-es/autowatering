package com.home.autowatering.api.rest

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractController {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AbstractController::class.java)
        val OK: String = "OK"
    }
}