package com.home.autowatering.controller

import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.StatusType
import com.home.autowatering.exception.AutowateringException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractController {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AbstractController::class.java)
    }

    protected fun <R> execute(block: () -> Response<R>): Response<R> =
        try {
            block.invoke()
        } catch (exc: AutowateringException) {
            LOGGER.info("error", exc)
            Response(
                exc.status,
                exc.message
            )
        } catch (exc: Exception) {
            LOGGER.error("internal error", exc)
            Response(
                StatusType.INTERNAL_ERROR,
                if (exc.message == null) exc.javaClass.name else exc.message!!
            )
        }

}