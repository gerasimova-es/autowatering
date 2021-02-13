package com.home.autowatering.api.rest

import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractController {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AbstractController::class.java)
    }

//    @ExceptionHandler
//    protected fun String response(exc: Exception):R {
//        LOGGER.error("request executing error", exc)
//        if (exc.message == null) exc.javaClass.name else exc.message!!
//
//    }
}