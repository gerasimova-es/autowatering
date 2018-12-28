package com.home.autowatering.controller

import com.home.autowatering.dto.response.Response
import com.home.autowatering.dto.response.ResponseStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Базовый контроллер
 */
abstract class AbstractController {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(AbstractController::class.java)
    }

    @ExceptionHandler
    protected fun <R> handle(exc: Exception): Response<R> {
        logger.error("request executing error", exc)
        return Response(
            ResponseStatus.ERROR,
            if (exc.message == null) exc.javaClass.name else exc.message!!
        )
    }
}