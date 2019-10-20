package com.home.autowatering.controller

import com.home.autowatering.controller.dto.TokensDto
import com.home.autowatering.controller.dto.UserDto
import com.home.autowatering.controller.dto.response.Response
import org.apache.commons.lang.Validate
import java.util.*

class UserController : AbstractController() {
    fun signup(user: UserDto) =
        execute {
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