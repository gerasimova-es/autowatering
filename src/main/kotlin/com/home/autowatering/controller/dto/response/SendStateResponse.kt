package com.home.autowatering.controller.dto.response

class SendStateResponse : Response<String> {
    constructor(payload: String) : super(payload)
    constructor(status: ResponseStatus, message: String) : super(status, message)

}