package com.home.autowatering.invoker.dto.state

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class GroundStateDto {

    var humidity: Int? = null

    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "dd-MM-yyyy'T'HH:mm:ss"
    )
    var lastCheck: Date? = null

    constructor()

    constructor(humidity: Int?, lastCheck: Date?) {
        this.humidity = humidity
        this.lastCheck = lastCheck
    }


}