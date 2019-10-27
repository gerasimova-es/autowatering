package com.home.autowatering.controller

import com.home.autowatering.controller.dto.TokensDto
import com.home.autowatering.controller.dto.UserDto
import com.home.autowatering.controller.dto.response.Response
import io.vertx.core.Future
import org.apache.commons.lang.Validate
import java.util.*

class UserController : AbstractController() {
    fun signup(user: UserDto): Future<Response<TokensDto>> =
        Future.future {
            Validate.noNullElements(
                arrayOf(user.login, user.password)
            )
            Response(
                TokensDto(
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString()
                )
            )
        }
}