package com.home.autowatering.invoker.dto.state

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class LightStateDto {

    var status: Boolean? = null

    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "dd-MM-yyyy'T'HH:mm:ss"
    )
    var lastCheck: Date? = null

    constructor()

    constructor(status: Boolean?, lastCheck: Date?) {
        this.status = status
        this.lastCheck = lastCheck
    }

}