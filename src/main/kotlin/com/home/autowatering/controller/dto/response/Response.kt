package com.home.autowatering.controller.dto.response

open class Response<T> {
    var status: StatusType? = null
    var message: String? = null
    var payload: T? = null

    constructor()

    constructor(payload: T) {
        this.status = StatusType.SUCCESS
        this.message = "message was handled successfully"
        this.payload = payload
    }

    constructor(status: StatusType, message: String) {
        this.status = status
        this.message = message
    }
}