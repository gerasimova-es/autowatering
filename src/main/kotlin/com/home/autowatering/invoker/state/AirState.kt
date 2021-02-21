package com.home.autowatering.invoker.state

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
class AirState {

    var humidity: Int? = null
    var temp: Int? = null
    var needWater: Boolean? = null

    @field:JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "dd-MM-yyyy'T'HH:mm:ss"
    )
    var date: Date? = null
}