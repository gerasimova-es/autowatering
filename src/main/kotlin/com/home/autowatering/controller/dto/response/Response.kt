package com.home.autowatering.controller.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
open class Response<T> {
    var status: StatusType? = null
    var message: String? = null
    var payload: T? = null

    constructor(){
        this.status = StatusType.SUCCESS
        this.message = "message was handled successfully"
    }

    constructor(payload: T) : this(){
        this.payload = payload
    }

    constructor(status: StatusType, message: String?) {
        this.status = status
        this.message = message
    }
}