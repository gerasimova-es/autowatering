package com.home.autowatering.controller.dto.response

open class Response<T> {
    var status: ResponseStatus = ResponseStatus.SUCCESS
    var message: String = "message was handled successfully"
    var payload: T? = null

    constructor(payload: T) {
        this.payload = payload
    }

    constructor(status: ResponseStatus, message: String) {
        this.status = status
        this.message = message
    }
}