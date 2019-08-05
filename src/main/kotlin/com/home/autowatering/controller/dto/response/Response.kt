package com.home.autowatering.controller.dto.response

open class Response<T> {
    val status: ResponseStatus
    val message: String
    var payload: T? = null

    constructor(payload: T) {
        this.status = ResponseStatus.SUCCESS
        this.message = "message was handled successfully"
        this.payload = payload
    }

    constructor(status: ResponseStatus, message: String) {
        this.status = status
        this.message = message
    }
}