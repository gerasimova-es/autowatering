package com.home.autowatering.dto.response

class BaseResponse<T> {
    var status: ResponseStatus = ResponseStatus.SUCCESS
    var message: String = "сообщение обработано успешно"
    var payload: T? = null

    constructor(payload: T) {
        this.payload = payload
    }

    constructor(status: ResponseStatus, message: String) {
        this.status = status
        this.message = message
    }
}