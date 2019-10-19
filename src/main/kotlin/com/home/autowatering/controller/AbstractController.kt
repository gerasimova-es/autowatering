package com.home.autowatering.controller

import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.ResponseStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractController {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AbstractController::class.java)
    }

    protected fun <R> execute(block: () -> Response<R>): Response<R> =
        try {
            block.invoke()
        } catch (exc: Exception) {
            LOGGER.error("request executing error", exc)
            Response(
                ResponseStatus.ERROR,
                if (exc.message == null) exc.javaClass.name else exc.message!!
            )
        }

}