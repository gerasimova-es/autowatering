package com.home.autowatering.controller

import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.ResponseStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler

abstract class AbstractController {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AbstractController::class.java)
    }

    @ExceptionHandler
    protected fun <R> response(exc: Exception): Response<R> {
        LOGGER.error("request executing error", exc)
        return Response(
            ResponseStatus.ERROR,
            if (exc.message == null) exc.javaClass.name else exc.message!!
        )
    }
}