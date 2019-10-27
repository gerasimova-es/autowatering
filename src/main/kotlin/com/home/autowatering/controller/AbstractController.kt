package com.home.autowatering.controller

import com.home.autowatering.controller.dto.response.Response
import com.home.autowatering.controller.dto.response.StatusType
import com.home.autowatering.exception.AutowateringException
import io.vertx.core.Future
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class AbstractController {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AbstractController::class.java)
    }

    protected fun <R> Future<Response<R>>.handleFailure(): Future<Response<R>> {
        return this.setHandler { result ->
            if (result.succeeded()) {
                Future.succeededFuture(result.result())
            }
            val response = when (val error = this.cause()) {
                is AutowateringException -> {
                    LOGGER.debug("error", error)
                    Response<R>(error.status, error.message)
                }
                else -> {
                    LOGGER.error("internal error", error)
                    Response(
                        StatusType.INTERNAL_ERROR,
                        if (error.message == null) error.javaClass.name else error.message!!
                    )
                }
            }
            Future.succeededFuture(response)
        }
    }
}